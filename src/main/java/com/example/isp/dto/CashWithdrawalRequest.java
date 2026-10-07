package com.example.isp.dto;

import java.math.BigDecimal;

public record CashWithdrawalRequest(
        String accountId,
        String atmTerminalId,
        BigDecimal amount
) {}