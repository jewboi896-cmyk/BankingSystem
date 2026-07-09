package com.bank.api.dto.request;

import java.math.BigDecimal;

public record TransactionRequest(BigDecimal amount, String description) {}
