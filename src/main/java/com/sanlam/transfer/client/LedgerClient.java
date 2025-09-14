package com.sanlam.transfer.client;

import com.sanlam.transfer.client.dto.LedgerRequest;
import com.sanlam.transfer.client.dto.LedgerResponse;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ledgerClient", url = "${ledger.url}")
public interface LedgerClient {
    @PostMapping("/ledger/transfer")
    ResponseEntity<LedgerResponse> sendTransfer(@RequestBody LedgerRequest req) throws FeignException.FeignClientException;
}

