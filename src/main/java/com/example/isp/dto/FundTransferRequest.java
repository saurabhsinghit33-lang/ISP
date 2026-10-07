package com.example.isp.dto;

import java.math.BigDecimal;

public record FundTransferRequest(
        String sourceAccountId,
        String targetAccountId,
        String beneficiaryIfsc,
        BigDecimal amount
) {}