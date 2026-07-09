package com.bank.exception;

public class ForbiddenException extends BankingException {
    public ForbiddenException(String reason) {
        super(reason);
    }
}
