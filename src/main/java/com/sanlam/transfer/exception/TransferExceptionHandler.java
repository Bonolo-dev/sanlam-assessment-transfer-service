package com.sanlam.transfer.exception;

import com.sanlam.transfer.dto.TransferErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransferExceptionHandler {

    int MAX_BATCH_SIZE = 20;

    @ExceptionHandler(TransferNotFoundException.class)
    public ResponseEntity<TransferErrorResponse> handleTransferNotFound(TransferNotFoundException ex) {
        TransferErrorResponse error = new TransferErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BatchException.class)
    public ResponseEntity<TransferErrorResponse> handleLargeBatchRequest() {
        String DEFAULT_LARGE_BATCH_MESSAGE = "Either Batch size exceeds the maximum limit of " + MAX_BATCH_SIZE + " transfers or Batch is empty.";
        TransferErrorResponse error = new TransferErrorResponse(DEFAULT_LARGE_BATCH_MESSAGE, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
