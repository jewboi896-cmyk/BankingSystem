package com.bank.exception;

public class WithdrawalLimitExceededException extends BankingException {
    public WithdrawalLimitExceededException(int accountLimit, int currentNumber) {
        super("Monthly withdrawal limit reached: " + currentNumber + "/" + accountLimit + " withdrawals used.");
    }
}
