package com.example.isp.service;

import com.example.isp.contract.CreditFacilityManageable;
import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BranchOperationsService implements CreditFacilityManageable {

    @Override
    public TransactionReceipt adjustOverdraftLimit(CreditAdjustmentRequest request) {
        System.out.println("Branch Officer " + request.officerEmployeeId() + 
                " authorized new OD limit of $" + request.newOverdraftLimit() + 
                " for account " + request.accountId());

        return new TransactionReceipt(
                "OD-" + UUID.randomUUID().toString().substring(0, 8),
                request.accountId(),
                "APPROVED",
                "Overdraft limit updated",
                LocalDateTime.now()
        );
    }
}
