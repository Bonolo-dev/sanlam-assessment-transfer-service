package com.sanlam.transfer.exception;

import com.sanlam.transfer.dto.TransferErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransferExceptionHandler {
    @ExceptionHandler(TransferNotFoundException.class)
    public ResponseEntity<TransferErrorResponse> handleTransferNotFound(TransferNotFoundException ex) {
        TransferErrorResponse error = new TransferErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
