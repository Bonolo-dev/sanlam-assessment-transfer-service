package com.sanlam.transfer.controller;

import com.sanlam.transfer.dto.TransferRequest;
import com.sanlam.transfer.dto.TransferResponse;
import com.sanlam.transfer.exception.TransferNotFoundException;
import com.sanlam.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody TransferRequest request, @RequestHeader("Idempotency-Key") String key) {
        return ResponseEntity.ok(transferService.initiateTransfer(request, key));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransfer(@PathVariable UUID id) {
        return ResponseEntity.ok(transferService.getTransferStatus(id));
    }


}
