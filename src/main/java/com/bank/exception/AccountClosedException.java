package com.bank.exception;

import java.util.UUID;

public class AccountClosedException extends BankingException {
    public AccountClosedException(UUID accountID) {
        super("Account: " + accountID + " has been closed. No transactions are allowed to or from this account.");
    }
}