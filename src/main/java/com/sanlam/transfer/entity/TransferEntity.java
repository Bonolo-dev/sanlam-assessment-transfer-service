package com.sanlam.transfer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID transferId;
    private String idempotencyKey;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    private String response;
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum Status { PENDING, COMPLETED, FAILED, PENDING_IN_BATCH }
}
