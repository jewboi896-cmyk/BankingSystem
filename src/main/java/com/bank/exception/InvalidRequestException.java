package com.bank.exception;

public class InvalidRequestException extends BankingException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
