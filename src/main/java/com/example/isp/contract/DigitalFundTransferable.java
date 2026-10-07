package com.example.isp.contract;

import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;

public interface DigitalFundTransferable {
    TransactionReceipt transferNeft(FundTransferRequest request);
    TransactionReceipt transferImps(FundTransferRequest request);
}