package com.bank.api.dto.request;

import java.math.BigDecimal;

public record CreateCheckingRequest(BigDecimal overdraftLimit) {}
