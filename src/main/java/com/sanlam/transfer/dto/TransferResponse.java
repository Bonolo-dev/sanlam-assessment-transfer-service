package com.sanlam.transfer.dto;

import com.sanlam.transfer.entity.TransferEntity;

import java.util.UUID;

public record TransferResponse(UUID transferId, TransferEntity.Status status) { }
