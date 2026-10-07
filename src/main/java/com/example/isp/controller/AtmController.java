package com.example.isp.controller;

import com.example.isp.contract.CashWithdrawable;
import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/atm")
public class AtmController {

    private final CashWithdrawable cashService;

    public AtmController(CashWithdrawable cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionReceipt> withdraw(@RequestBody CashWithdrawalRequest request) {
        return ResponseEntity.ok(cashService.withdrawCash(request));
    }
}
