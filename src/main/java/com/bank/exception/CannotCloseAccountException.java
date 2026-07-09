package com.bank.exception;

import java.util.UUID;

public class CannotCloseAccountException extends BankingException {
    public CannotCloseAccountException(UUID accountID) {
        super("Account with account id: " + accountID + " cannot be closed.");
    }
    public CannotCloseAccountException(UUID accountID, String reason) {
        super("Account with account id: " + accountID + " cannot be closed. Reason: " + reason);
    }
}
