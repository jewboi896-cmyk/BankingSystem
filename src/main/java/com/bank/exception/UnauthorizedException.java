package com.bank.exception;

public class UnauthorizedException extends BankingException {
    public UnauthorizedException(String reason) {
        super(reason);
    }
}
