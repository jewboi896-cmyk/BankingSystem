package com.bank.api.controller;

import com.bank.account.*;
import com.bank.api.AccountOwnershipGuard;
import com.bank.api.dto.response.AccountResponse;
import com.bank.api.dto.request.CreateCheckingRequest;
import com.bank.api.dto.request.CreateSavingsRequest;
import com.bank.auth.jwt.RequestContext;
import com.bank.exception.BankingException;
import io.javalin.http.Context;

import java.util.List;
import java.util.UUID;

public class AccountController {
    private final AccountService accountService;
    private final AccountOwnershipGuard guard;

    public AccountController(AccountService accountService,
                             AccountOwnershipGuard guard) {
        this.accountService = accountService;
        this.guard = guard;
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void createChecking(Context ctx) throws BankingException {
        UUID userID = RequestContext.userID(ctx);
        CreateCheckingRequest cCReq = ctx.bodyAsClass(CreateCheckingRequest.class);
        Account account = accountService.createCheckingAccount(userID,
                cCReq.overdraftLimit());
        ctx.status(201).json(AccountResponse.fromAccount(account));
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void createSavings(Context ctx) throws BankingException {
        UUID userID = RequestContext.userID(ctx);
        CreateSavingsRequest csReq = ctx.bodyAsClass(CreateSavingsRequest.class);
        Account account = accountService.createSavingsAccount(userID,
                csReq.interestRate(), csReq.withdrawalLimit());
        ctx.status(201).json(AccountResponse.fromAccount(account));
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void getAccount(Context ctx) throws BankingException {
        UUID accountID = UUID.fromString(ctx.pathParam("id"));
        Account account = guard.requireOwnedAccount(ctx, accountID);
        ctx.json(AccountResponse.fromAccount(account));
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     */
    public void listMyAccounts(Context ctx) {
        UUID userID = RequestContext.userID(ctx);
        List<Account> accounts = accountService.getAllUserAccounts(userID);
        ctx.json(accounts.stream().map(AccountResponse::fromAccount).toList());
    }
}
