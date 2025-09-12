package com.sanlam.transfer.service;

import com.sanlam.transfer.dto.TransferRequest;
import com.sanlam.transfer.dto.TransferResponse;
import com.sanlam.transfer.entity.TransferEntity;
import com.sanlam.transfer.exception.TransferNotFoundException;
import com.sanlam.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepo;
//    private final TransferEventProducer producer;

    @Transactional
    public TransferResponse initiateTransfer(TransferRequest req, String idempotencyKey) {

        return transferRepo.findByIdempotencyKey(idempotencyKey)
                .map(existing -> new TransferResponse(existing.getTransferId(), existing.getStatus()))
                .orElseGet(() -> {
                    TransferEntity transfer = new TransferEntity();
                    transfer.setTransferId(UUID.randomUUID());
                    transfer.setIdempotencyKey(idempotencyKey);
                    transfer.setFromAccountId(req.fromAccountId());
                    transfer.setToAccountId(req.toAccountId());
                    transfer.setAmount(req.amount());
                    transfer.setStatus(TransferEntity.Status.PENDING);

                    transferRepo.save(transfer);

//                    producer.sendTransferRequest(new TransferRequestedEvent(transfer));
                    return new TransferResponse(transfer.getTransferId(), transfer.getStatus());
                });
    }

    public TransferResponse getTransferStatus(UUID id) {
        TransferEntity transfer = transferRepo.findByTransferId(id)
                .orElseThrow(() -> new TransferNotFoundException(id));
        return new TransferResponse(transfer.getTransferId(), transfer.getStatus());
    }
}
