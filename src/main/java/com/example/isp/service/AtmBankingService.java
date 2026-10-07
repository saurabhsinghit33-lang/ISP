package com.example.isp.service;

import com.example.isp.contract.CashWithdrawable;
import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AtmBankingService implements CashWithdrawable {

    @Override
    public TransactionReceipt withdrawCash(CashWithdrawalRequest request) {
        System.out.println("Dispensing physical cash of $" + request.amount() + " from ATM: " + request.atmTerminalId());
        return new TransactionReceipt(
                "ATM-" + UUID.randomUUID().toString().substring(0, 8),
                request.accountId(),
                "COMPLETED",
                "Cash dispensed successfully",
                LocalDateTime.now()
        );
    }
}
