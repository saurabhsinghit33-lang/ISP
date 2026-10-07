package com.example.isp.contract;

import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;

public interface CreditFacilityManageable {
    TransactionReceipt adjustOverdraftLimit(CreditAdjustmentRequest request);
}