package com.bank.repository;

import com.bank.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void saveTransaction(Transaction transaction);
    @NotNull Optional<Transaction> findTransactionById(UUID transactionId);
    // stores both source and destination account
    List<Transaction> findTransactionByAccountId(UUID accountId);
    List<Transaction> findAllTransactions();
}
