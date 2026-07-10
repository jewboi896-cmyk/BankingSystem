package com.bank.api.controller;

import com.bank.api.AccountOwnershipGuard;
import com.bank.api.dto.request.TransactionRequest;
import com.bank.api.dto.request.TransferRequest;
import com.bank.api.dto.response.TransactionResponse;
import com.bank.exception.BankingException;
import com.bank.transaction.Transaction;
import com.bank.transaction.TransactionService;
import com.bank.transaction.TransactionType;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TransactionController {
    private final TransactionService transactionService;
    private final AccountOwnershipGuard guard;

    public TransactionController(TransactionService transactionService,
                                 AccountOwnershipGuard guard) {
        this.transactionService = transactionService;
        this.guard = guard;
    }

    /**
     * @summary
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void deposit(@NotNull Context ctx) throws BankingException {
        UUID accountID = UUID.fromString(ctx.pathParam("id"));
        guard.requireOwnedAccount(ctx, accountID);
        TransactionRequest tReq = ctx.bodyAsClass(TransactionRequest.class);
        Transaction tx = transactionService.depositFunds(accountID,
                tReq.amount(), tReq.description());
        ctx.status(201).json(toResponse(tx));
    }

    /**
     * @summary
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void withdraw(@NotNull Context ctx) throws BankingException {
        UUID accountID = UUID.fromString(ctx.pathParam("id"));
        guard.requireOwnedAccount(ctx, accountID);
        TransactionRequest tReq = ctx.bodyAsClass(TransactionRequest.class);
        Transaction tx = transactionService.withdrawFunds(accountID,
                tReq.amount(), tReq.description());
        ctx.status(201).json(toResponse(tx));
    }

    /**
     * @summary
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void transfer(@NotNull Context ctx) throws BankingException {
        TransferRequest trReq = ctx.bodyAsClass(TransferRequest.class);
        guard.requireOwnedAccount(ctx, trReq.srcID());
        Transaction tx = transactionService.transferFunds(trReq.srcID(),
                trReq.destID(), trReq.amount(), trReq.description());
        ctx.status(201).json(toResponse(tx));
    }

    /**
     * @summary
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void getHistory(@NotNull Context ctx) throws BankingException {
        UUID accountID = UUID.fromString(ctx.pathParam("id"));
        guard.requireOwnedAccount(ctx, accountID);
        List<Transaction> history = transactionService.getTransactionHistoryForAccount(accountID);
        ctx.json(history.stream().map(this::toResponse).toList());
    }

    /**
     * @summary
     * @author Derek Homel
     * @param transaction the transaction to be responded to
     * @return returns a switch on the different transaction types:
     * DEPOSIT, WITHDRAW, FEE, and TRANSFER
     */
    private @NotNull TransactionResponse toResponse(
            @NotNull Transaction transaction) {
        return switch (transaction.getTransactionType()) {
            case TransactionType.DEPOSIT -> new TransactionResponse(
                    transaction.getTransactionId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    null,
                    transaction.getDestinationAccountId(),
                    transaction.getTransactionStatus(),
                    transaction.getTimeCreated(),
                    transaction.getTransactionDescription()
                );
            case TransactionType.WITHDRAW, TransactionType.FEE ->
                    new TransactionResponse(
                        transaction.getTransactionId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getSourceAccountId(),
                        null,
                        transaction.getTransactionStatus(),
                        transaction.getTimeCreated(),
                        transaction.getTransactionDescription()
                );
            case TransactionType.TRANSFER -> new TransactionResponse(
                    transaction.getTransactionId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getSourceAccountId(),
                    transaction.getDestinationAccountId(),
                    transaction.getTransactionStatus(),
                    transaction.getTimeCreated(),
                    transaction.getTransactionDescription()
                );
        };
    }
}
