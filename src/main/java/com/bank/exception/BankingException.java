package com.bank.exception;

public abstract class BankingException extends Exception {
    public BankingException(String message) {
        super(message);
    }
    public BankingException(String message, Throwable cause) { super(message, cause); }
}
