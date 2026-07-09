package com.bank.exception;

public class AccountLockedException extends BankingException {
    public AccountLockedException(int minutesRemaining) {
        super("Account locked: Check back in " + minutesRemaining + " minute(s)");
    }
}
