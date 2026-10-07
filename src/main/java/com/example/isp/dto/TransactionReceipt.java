package com.example.isp.dto;

import java.time.LocalDateTime;

public record TransactionReceipt(
        String referenceNumber,
        String accountId,
        String status,
        String remarks,
        LocalDateTime timestamp
) {}