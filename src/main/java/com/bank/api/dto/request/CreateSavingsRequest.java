package com.bank.api.dto.request;

import java.math.BigDecimal;

public record CreateSavingsRequest(BigDecimal interestRate, int withdrawalLimit) {
}
