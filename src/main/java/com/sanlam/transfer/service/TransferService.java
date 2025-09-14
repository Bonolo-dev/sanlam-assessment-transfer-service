package com.sanlam.transfer.service;

import com.sanlam.transfer.client.LedgerClient;
import com.sanlam.transfer.client.dto.LedgerRequest;
import com.sanlam.transfer.client.dto.LedgerResponse;
import com.sanlam.transfer.dto.TransferRequest;
import com.sanlam.transfer.dto.TransferResponse;
import com.sanlam.transfer.entity.TransferEntity;
import com.sanlam.transfer.exception.BatchException;
import com.sanlam.transfer.exception.TransferNotFoundException;
import com.sanlam.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepo;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final LedgerClient ledgerClient;
//    private final TransferEventProducer producer;

    public TransferEntity persistTransfer(TransferRequest req, String idempotencyKey) {

        TransferEntity transfer = new TransferEntity();
        transfer.setTransferId(UUID.randomUUID());
        transfer.setIdempotencyKey(idempotencyKey);
        transfer.setFromAccountId(req.fromAccountId());
        transfer.setToAccountId(req.toAccountId());
        transfer.setAmount(req.amount());
        transfer.setStatus(TransferEntity.Status.PENDING);

        return transferRepo.save(transfer);
    }

    @Transactional
    public TransferResponse initiateTransfer(TransferRequest req, String idempotencyKey) {
        return transferRepo.findByIdempotencyKey(idempotencyKey)
                .map(existing -> new TransferResponse(existing.getTransferId(), existing.getStatus()))
                .orElseGet(() -> {
                    TransferEntity transferEntity = persistTransfer(req, idempotencyKey);

                    ResponseEntity<LedgerResponse> clientResponse = ledgerClient.sendTransfer(LedgerRequest.builder()
                            .fromAccountId(req.fromAccountId())
                            .toAccountId(req.toAccountId())
                            .amount(req.amount())
                            .build());

                    if (clientResponse.getStatusCode().is2xxSuccessful() && clientResponse.getBody() != null && clientResponse.getBody().success()) {
                        transferEntity.setStatus(TransferEntity.Status.COMPLETED);
                    } else {
                        transferEntity.setStatus(TransferEntity.Status.FAILED);
                    }
                    transferRepo.save(transferEntity);
                    return new TransferResponse(transferEntity.getTransferId(), transferEntity.getStatus());
                });
    }

    public TransferResponse getTransferStatus(UUID id) {
        TransferEntity transfer = transferRepo.findByTransferId(id)
                .orElseThrow(() -> new TransferNotFoundException(id));
        return new TransferResponse(transfer.getTransferId(), transfer.getStatus());
    }


    public List<TransferResponse> processBatch(List<TransferRequest> requests, String batchKey) {
        if (requests.size() > 20) {
            throw new BatchException();
        }

        List<CompletableFuture<TransferResponse>> futures = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            TransferRequest req = requests.get(i);
            String idempotencyKey = batchKey + "-" + i;

            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    return initiateTransfer(req, idempotencyKey);
                } catch (Exception e) {
                    return new TransferResponse(null, TransferEntity.Status.FAILED);
                }
            }, executor));
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
