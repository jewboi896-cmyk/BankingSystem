package com.bank.exception;

import java.util.UUID;

public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(UUID accountID) {
        super("Account: " + accountID + " does not exist.");
    }
}
