package com.bank.exception;

public class DuplicateUsernameException extends BankingException {
    public DuplicateUsernameException(String username) {
        super("Account with username: " + username + " already exists.");
    }
}
