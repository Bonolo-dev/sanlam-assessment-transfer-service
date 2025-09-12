package com.sanlam.transfer.exception;

import java.util.UUID;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException() {
        super("Transfer not found: ");
    }
    public TransferNotFoundException(UUID id) {
        super("Transfer Id not found: " + id);
    }

}
