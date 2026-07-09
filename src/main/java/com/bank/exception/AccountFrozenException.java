package com.bank.exception;

import java.util.UUID;

public class AccountFrozenException extends BankingException {
    public AccountFrozenException(UUID accountID) {
        super("Account: " + accountID + " has been frozen. No transactions are allowed to or from this account.");
    }
}
