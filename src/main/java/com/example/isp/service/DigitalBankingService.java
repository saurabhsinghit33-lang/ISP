package com.example.isp.service;

import com.example.isp.contract.DigitalFundTransferable;
import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DigitalBankingService implements DigitalFundTransferable {

    @Override
    public TransactionReceipt transferNeft(FundTransferRequest request) {
        System.out.println("Queued NEFT batch payment: $" + request.amount() + " to " + request.targetAccountId());
        return buildReceipt(request.sourceAccountId(), "NEFT transfer queued");
    }

    @Override
    public TransactionReceipt transferImps(FundTransferRequest request) {
        System.out.println("Executed real-time IMPS settlement: $" + request.amount() + " to " + request.targetAccountId());
        return buildReceipt(request.sourceAccountId(), "IMPS transfer completed instantly");
    }

    private TransactionReceipt buildReceipt(String accountId, String remarks) {
        return new TransactionReceipt(
                "DIGI-" + UUID.randomUUID().toString().substring(0, 8),
                accountId,
                "COMPLETED",
                remarks,
                LocalDateTime.now()
        );
    }
}