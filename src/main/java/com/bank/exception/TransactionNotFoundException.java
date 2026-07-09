package com.bank.exception;

import java.util.UUID;

public class TransactionNotFoundException extends BankingException {
    public TransactionNotFoundException(UUID transactionID) {
        super("Transaction: " + transactionID + " does not exist.");
    }
}
