package com.bank.api;

import com.bank.account.Account;
import com.bank.account.AccountService;
import com.bank.auth.jwt.RequestContext;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.BankingException;
import com.bank.exception.ForbiddenException;
import io.javalin.http.Context;

import java.util.UUID;

public class AccountOwnershipGuard {
    private final AccountService accountService;

    public AccountOwnershipGuard(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @author Derek Homel
     * @summary
     * @param ctx the context for the request
     * @param accountID the account ID of the specified account
     * @return returns the accountID of the specified account
     * @throws AccountNotFoundException throws if the account cannot be found
     * @throws ForbiddenException throws if not authorized
     */
    public Account requireOwnedAccount(Context ctx, UUID accountID)
            throws AccountNotFoundException, ForbiddenException {
        Account account = accountService.getAccountByID(accountID);
        if (!account.getUserID().equals(RequestContext.userID(ctx))) {
            throw new ForbiddenException("Account does not belong to caller");
        }
        return account;
    }
}
