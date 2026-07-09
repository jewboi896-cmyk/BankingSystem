package com.bank.repository.inMem;

import com.bank.repository.TransactionRepository;
import com.bank.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> transactions = new HashMap<>();

    /**
     * @author Derek Homel
     * @summary
     * @param transaction the transaction to save
     */
    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
    }

    /**
     * @author Derek Homel
     * @summary
     * @param transactionID the transactionID to find
     * @return returns an Optional of the transactionID
     */
    @Override
    public @NotNull Optional<Transaction> findTransactionById(UUID transactionID) {
        return Optional.ofNullable(transactions.get(transactionID));
    }

    /**
     * @author Derek Homel
     * @summary
     * @param accountId the accountID to filter on to find the transaction
     * @return returns a List of Collections that is filtered by the source
     * and destination accounts
     */
    @Override
    public List<Transaction> findTransactionByAccountId(UUID accountId) {
        return transactions.values().stream().
                filter(t -> accountId.equals(t.getSourceAccountId())
                || accountId.equals(t.getDestinationAccountId())).toList();
    }

    /**
     * @author Derek Homel
     * @summary
     * @return returns a List of Collections for all the transactions
     */
    @Override
    public List<Transaction> findAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
}
