package com.bank.exception;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AccountFrozenException extends BankingException {

    public enum FrozenOp {
        TRANSACTION,
        CLOSURE;

        @Contract(pure = true)
         private @NotNull String describe() {
            return switch (this) {
                case CLOSURE -> "Account must be unfrozen before it can be " +
                        "closed";
                case TRANSACTION -> "Account must be unfrozen before any " +
                        "transactions are allowed to happen";
            };
        }
    }
    public AccountFrozenException(UUID accountID, @NotNull FrozenOp op) {
        super("Account: " + accountID + " has been frozen. " + op.describe());
    }
}
