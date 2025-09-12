package com.sanlam.transfer.repository;

import com.sanlam.transfer.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<TransferEntity,Long> {
    Optional<TransferEntity> findByIdempotencyKey(String idempotencyKey);
    Optional<TransferEntity> findByTransferId(UUID transferId);
}
