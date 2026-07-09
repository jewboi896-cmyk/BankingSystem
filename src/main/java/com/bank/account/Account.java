package com.bank.account;

import com.bank.exception.AccountClosedException;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.BankingException;
import com.bank.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.bank.account.AccountStatus.*;

// sealed because of a set number of subclasses so inheritance is controlled
public sealed abstract class Account permits CheckingAccount, SavingsAccount {
    private final UUID accountID;
    private final UUID userID;
    private BigDecimal accountBalance;
    private AccountStatus accountStatus;
    private final LocalDateTime timeCreated;

    public Account(UUID userID) {
        this.accountID = UUID.randomUUID();
        this.userID = userID;
        this.accountBalance = BigDecimal.ZERO;
        this.accountStatus = AccountStatus.ACTIVE;
        this.timeCreated = LocalDateTime.now();
    }

    public UUID getAccountID() { return accountID; }
    public UUID getUserID() { return userID; }
    public BigDecimal getAccountBalance() { return accountBalance; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public LocalDateTime getTimeCreated() { return timeCreated; }

    void setAccountBalance(BigDecimal balance) { this.accountBalance = balance; }
    void setAccountStatus(AccountStatus status) { this.accountStatus = status; }

    public abstract AccountType getAccountType();
    public abstract void validateWithdrawal(BigDecimal amount) throws BankingException;

    // checks if AccountStatus is ACTIVE and amount is <= 0

    /**
     * @summary
     * @author Derek Homel
     * @param amount amount to validate
     * @throws AccountFrozenException throws when account is frozen
     * @throws AccountClosedException throws when account is closed
     * @throws InvalidAmountException throws when amount is less than or equal to
     * zero
     */
    public void validateDeposit(BigDecimal amount) throws AccountFrozenException,
            AccountClosedException, InvalidAmountException {
        validateAccountActiveAndAmount(amount);
    }

    /**
     * @summary
     * @author Derek Homel
     * @param amount amount to be transferred
     * @param destination destination account for the transfer
     * @throws IllegalArgumentException throws if arguments are not recognized
     * @throws BankingException needed because validateWithdrawal throws this
     */
    public void validateTransfer(BigDecimal amount, Account destination) throws
            IllegalArgumentException, BankingException {
        this.validateWithdrawal(amount);

        if (destination == null) {
            throw new IllegalArgumentException("Account destination cannot be null");
        }

        if (destination.getAccountStatus() == FROZEN) {
            throw new AccountFrozenException(destination.accountID);
        }
        if (destination.getAccountStatus() == CLOSED) {
            throw new AccountClosedException(destination.accountID);
        }
    }

   void withdrawFunds(BigDecimal amount) { this.accountBalance = this.accountBalance.subtract(amount); }
   void depositFunds(BigDecimal amount) { this.accountBalance = this.accountBalance.add(amount); }

    /**
     * @summary
     * @author Derek Homel
     * @param amount amount to validate
     * @throws AccountFrozenException throws if specified account is frozen
     * @throws AccountClosedException throws if specified account is closed
     * @throws InvalidAmountException throws if the accounts amount is less
     * than or equal to zero
     */
    protected void validateAccountActiveAndAmount(BigDecimal amount) throws AccountFrozenException,
            AccountClosedException, InvalidAmountException {
        if (FROZEN == getAccountStatus()) throw new AccountFrozenException(getAccountID());
        if (CLOSED == getAccountStatus()) throw new AccountClosedException(getAccountID());
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new InvalidAmountException(amount);
    }
}
