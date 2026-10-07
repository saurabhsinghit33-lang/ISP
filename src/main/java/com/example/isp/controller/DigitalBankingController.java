package com.example.isp.controller;

import com.example.isp.contract.DigitalFundTransferable;
import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/digital")
public class DigitalBankingController {

    private final DigitalFundTransferable transferService;

    public DigitalBankingController(DigitalFundTransferable transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/neft")
    public ResponseEntity<TransactionReceipt> transferNeft(@RequestBody FundTransferRequest request) {
        return ResponseEntity.ok(transferService.transferNeft(request));
    }

    @PostMapping("/imps")
    public ResponseEntity<TransactionReceipt> transferImps(@RequestBody FundTransferRequest request) {
        return ResponseEntity.ok(transferService.transferImps(request));
    }
}
