package com.sanlam.transfer.utility;

import com.sanlam.transfer.dto.TransferRequest;
import com.sanlam.transfer.entity.TransferEntity;
import com.sanlam.transfer.repository.TransferRepository;
import com.sanlam.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryScheduler {
    private final TransferRepository transferRepo;
    private final TransferService transferService;

    // Run every 1 minute (you can adjust)
    @Scheduled(fixedDelay = 60000)
    public void retryPendingTransfers() {
        List<TransferEntity> pendingTransfers =
                transferRepo.findByStatus(TransferEntity.Status.RETRY_PENDING);

        if (pendingTransfers.isEmpty()) {
            return;
        }

        log.info("🔄 Retrying {} pending transfers...", pendingTransfers.size());

        for (TransferEntity transfer : pendingTransfers) {
            try {
                TransferRequest req = new TransferRequest(
                        transfer.getFromAccountId(),
                        transfer.getToAccountId(),
                        transfer.getAmount()
                );

                transferService.initiateTransfer(req, transfer.getIdempotencyKey());
            } catch (Exception e) {
                log.warn("Retry failed for transfer {}: {}", transfer.getTransferId(), e.getMessage());
            }
        }
    }
}
