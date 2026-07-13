package com.bank.api.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCheckingRequest(BigDecimal overdraftLimit) {
}
