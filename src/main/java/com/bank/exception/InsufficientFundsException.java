package com.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends BankingException {
    public InsufficientFundsException(BigDecimal attemptedFunds, BigDecimal remainingFunds) {
        super("Insufficient funds: attempted " + attemptedFunds + ", funds. Remaining funds: " + remainingFunds + " funds");
    }
}
