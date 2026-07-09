package com.bank.repository.json;

import com.bank.file.FileHelper;
import com.bank.repository.TransactionRepository;
import com.bank.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class JsonTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> transactions;
    private final FileHelper<Transaction> fileHelper;

    public JsonTransactionRepository(FileHelper<Transaction> fileHelper) {
        this.fileHelper = fileHelper;
        this.transactions = fileHelper.loadFromFile();
    }

    @Override
    public List<Transaction> findTransactionByAccountId(UUID accountId) {
        return transactions.values().stream()
                .filter(t -> accountId.equals(t.getSourceAccountId())
                        || accountId.equals(t.getDestinationAccountId()))
                .collect(Collectors.toList());
    }

    @Override @NotNull
    public Optional<Transaction> findTransactionById(UUID transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
        fileHelper.writeToFile(transactions);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
}
