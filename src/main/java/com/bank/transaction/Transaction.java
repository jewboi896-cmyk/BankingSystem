package com.bank.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID transactionId;
    private final TransactionType transactionType;
    private final BigDecimal amount;
    private final UUID sourceAccountId;
    private final UUID destinationAccountId;
    private TransactionStatus transactionStatus;
    private final LocalDateTime timeCreated;
    private String transactionDescription;

    public Transaction(TransactionType type, BigDecimal amount, UUID sourceAccountId, UUID destinationAccountId, String transactionDescription) {
        transactionId = UUID.randomUUID();
        transactionType = type;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        transactionStatus = TransactionStatus.PENDING;
        timeCreated = LocalDateTime.now();
        this.transactionDescription = transactionDescription;
    }

    public TransactionType getTransactionType() { return transactionType; }
    public BigDecimal getAmount() { return amount; }
    public TransactionStatus getTransactionStatus() { return transactionStatus; }
    public LocalDateTime getTimeCreated() { return timeCreated; }
    public String getTransactionDescription() { return transactionDescription; }
    public void setTransactionStatus(TransactionStatus transactionStatus) { this.transactionStatus = transactionStatus; }
    public void setTransactionDescription(String transactionDescription) { this.transactionDescription = transactionDescription; }

    public UUID getTransactionId() { return transactionId; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public UUID getDestinationAccountId() { return destinationAccountId; }
}
