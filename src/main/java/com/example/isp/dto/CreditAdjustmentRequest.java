package com.example.isp.dto;

import java.math.BigDecimal;

public record CreditAdjustmentRequest(
        String accountId,
        String officerEmployeeId,
        BigDecimal newOverdraftLimit
) {}