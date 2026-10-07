package com.example.isp.contract;

import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;

public interface CashWithdrawable {
    TransactionReceipt withdrawCash(CashWithdrawalRequest request);
}
