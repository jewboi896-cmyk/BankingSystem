package com.bank.account;

import com.bank.exception.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

// final to tells the compiler that this class cannot be extended
public final class CheckingAccount extends Account {
    private BigDecimal overdraftLimit;

    public CheckingAccount(UUID userID, BigDecimal overdraftLimit) {
        super(userID);
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(UUID userID) {
        this(userID, BigDecimal.ZERO);
    }

    @Override
    public @NotNull AccountType getAccountType() { return AccountType.CHECKING; }

    public @NotNull BigDecimal getOverdraftLimit() { return overdraftLimit; }

    @Deprecated
    private void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    /**
     * @author Derek Homel
     * @param amount withdrawn amount to validate
     * @throws AccountFrozenException throws if the account that requested
     * the validation is frozen
     * @throws AccountClosedException throws if the account that requested the
     * validation has been closed
     * @throws InsufficientFundsException throws if the requesting account
     * does not have enough funds in the account to complete the withdrawal
     * @throws InvalidAmountException throws if the amount is less than or
     * equal to zero
     */
    @Override
    public void validateWithdrawal(BigDecimal amount) throws
            AccountFrozenException, AccountClosedException,
            InsufficientFundsException, InvalidAmountException {
        validateAccountActiveAndAmount(amount);
        BigDecimal balance = getAccountBalance();
        if (balance.subtract(amount).compareTo(overdraftLimit.negate()) < 0) {
            throw new InsufficientFundsException(amount, balance);
        }
    }
}
