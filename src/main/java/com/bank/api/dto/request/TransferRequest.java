package com.bank.api.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(UUID srcID, UUID destID,
                              BigDecimal amount, String description) {}
