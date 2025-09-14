package com.sanlam.transfer.client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LedgerRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
