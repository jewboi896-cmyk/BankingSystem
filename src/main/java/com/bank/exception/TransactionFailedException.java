package com.bank.exception;

public class TransactionFailedException extends BankingException {
    public TransactionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
