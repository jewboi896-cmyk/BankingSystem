package com.bank.api.dto.response;

import com.bank.transaction.TransactionStatus;
import com.bank.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(UUID transactionId,
                                  TransactionType transactionType,
                                  BigDecimal amount,
                                  UUID sourceAccountId,        // null for deposits
                                  UUID destinationAccountId,   // null for withdrawals
                                  TransactionStatus transactionStatus,
                                  LocalDateTime timeCreated,
                                  String description) {}