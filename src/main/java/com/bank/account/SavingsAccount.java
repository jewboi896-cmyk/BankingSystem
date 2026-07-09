package com.bank.account;

import com.bank.exception.BankingException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.WithdrawalLimitExceededException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.YearMonth;
import java.util.UUID;

// final to tells the compiler that this class cannot be extended
public final class SavingsAccount extends Account {
    private BigDecimal interestRate;
    private final int monthlyWithdrawalLimit;
    private int withdrawalsThisMonth;
    private YearMonth withdrawalPeriod;
    private transient Clock clock; // transient so gson doesnt serialize

    public SavingsAccount(UUID userID, BigDecimal interestRate,
                          int monthlyWithdrawalLimit) {
        this(userID, interestRate, monthlyWithdrawalLimit,
                Clock.systemDefaultZone());
    }

    SavingsAccount(UUID userID, BigDecimal interestRate, int
                           monthlyWithdrawalLimit, Clock clock) {
        super(userID);
        this.interestRate = interestRate;
        this.monthlyWithdrawalLimit = monthlyWithdrawalLimit;
        this.withdrawalsThisMonth = 0;
        this.clock = clock;
        this.withdrawalPeriod = YearMonth.now(clock);
    }

    public @NotNull BigDecimal getInterestRate() { return interestRate; }
    public int getMonthlyWithdrawalLimit() { return monthlyWithdrawalLimit; }
    private void resetMonthlyWithdrawals() { this.withdrawalsThisMonth = 0; }

    @Override
    public @NotNull AccountType getAccountType() { return AccountType.SAVINGS; }

    /**
     * @summary
     * @author Derek Homel
     * @return returns the system clock on first load
     */
    // Lazy-init to system clock on first read.
    private Clock getClock() {
        if (clock == null) clock = Clock.systemDefaultZone();
        return clock;
    }

    /**
     * @summary
     * @author Derek Homel
     */
    private void rolloverIfNeeded() {
        YearMonth now = YearMonth.now(getClock());
        if (!now.equals(withdrawalPeriod)) {
            resetMonthlyWithdrawals();
            withdrawalPeriod = now;
        }
    }

    /**
     * @summary
     * @author Derek Homel
     * @return returns the number of withdrawals this month
     */
    public int getWithdrawalsThisMonth() {
        rolloverIfNeeded();
        return withdrawalsThisMonth;
    }

    /**
     * @summary
     * @author Derek Homel
     */
    private void incrementMonthlyWithdrawals() {
        rolloverIfNeeded();
        this.withdrawalsThisMonth++;
    }

    /**
     * @summary
     * @author Derek Homel
     * @param amount amount to validate
     * @throws BankingException throws blanket exception so as not to
     * crowd method signature
     */
    @Override
    public void validateWithdrawal(BigDecimal amount) throws BankingException {
        validateAccountActiveAndAmount(amount);
        rolloverIfNeeded();
        if (withdrawalsThisMonth >= monthlyWithdrawalLimit) {
            throw new WithdrawalLimitExceededException(monthlyWithdrawalLimit, withdrawalsThisMonth);
        }
        BigDecimal balance = getAccountBalance();
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(amount, balance);
        }
    }

    /**
     * @summary
     * @author Derek Homel
     * @param amount amount to withdraw
     */
    @Override
    void withdrawFunds(BigDecimal amount) {
        super.withdrawFunds(amount);
        incrementMonthlyWithdrawals();
    }
}
