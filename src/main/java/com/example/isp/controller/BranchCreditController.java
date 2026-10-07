package com.example.isp.controller;

import com.example.isp.contract.CreditFacilityManageable;
import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/branch/credit")
public class BranchCreditController {

    private final CreditFacilityManageable creditService;

    public BranchCreditController(CreditFacilityManageable creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/overdraft")
    public ResponseEntity<TransactionReceipt> adjustOverdraft(@RequestBody CreditAdjustmentRequest request) {
        return ResponseEntity.ok(creditService.adjustOverdraftLimit(request));
    }
}
