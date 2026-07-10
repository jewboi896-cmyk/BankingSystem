package com.bank.api.dto.response;

import com.bank.account.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(UUID accountID,
                              UUID userID,
                              AccountType accountType,         // "CHECKING" or "SAVINGS"
                              AccountStatus accountStatus,
                              BigDecimal balance,
                              LocalDateTime createdAt,
                              // checking-only
                              BigDecimal overdraftLimit,
                              // savings-only
                              BigDecimal interestRate,
                              Integer monthlyWithdrawalLimit,
                              Integer withdrawalsThisMonth) {

    /**
     * @author Derek Homel
     * @summary
     * @param account the account being referenced
     * @return returns a switch on the specific account subclass
     */
    public static @NotNull AccountResponse fromAccount(@NotNull Account account) {
        return switch (account) {
            case CheckingAccount cAccount -> new AccountResponse(account.getAccountID(),
                    account.getUserID(), account.getAccountType(),
                    account.getAccountStatus(), account.getAccountBalance(),
                    account.getTimeCreated(), cAccount.getOverdraftLimit(),
                    null, null, null);
            case SavingsAccount sAccount -> new AccountResponse(account.getAccountID(),
                    account.getUserID(), account.getAccountType(),
                    account.getAccountStatus(), account.getAccountBalance(),
                    account.getTimeCreated(), null,
                    sAccount.getInterestRate(),
                    sAccount.getMonthlyWithdrawalLimit(),
                    sAccount.getWithdrawalsThisMonth());
        };
    }
}
