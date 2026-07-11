package com.bank.api;

import com.bank.api.dto.response.ErrorResponse;
import com.bank.exception.*;
import io.javalin.config.JavalinConfig;
import org.jetbrains.annotations.NotNull;

public class ExceptionMapper {
    /**
     * @author Derek Homel
     * @summary
     * @param config the Javalin config
     */
    public static void registerExceptionHandlers(@NotNull JavalinConfig config) {
        // Domain exceptions → 400 (business rule rejection)
        config.routes.exception(AccountFrozenException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "account_frozen", e.getMessage())));
        config.routes.exception(AccountClosedException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "account_closed", e.getMessage())));
        config.routes.exception(InvalidAmountException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "invalid_amount", e.getMessage())));
        config.routes.exception(InsufficientFundsException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "insufficient_funds", e.getMessage())));
        config.routes.exception(WithdrawalLimitExceededException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "withdrawal_limit_exceeded", e.getMessage())));
        config.routes.exception(DuplicateUsernameException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "duplicate_username", e.getMessage())));
        config.routes.exception(CannotCloseAccountException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "cannot_close_account", e.getMessage())));
        config.routes.exception(TransactionFailedException.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "transaction_failed", e.getMessage())));

        // Not found → 404
        config.routes.exception(AccountNotFoundException.class,
                (e, ctx) ->
                ctx.status(404).json(new ErrorResponse(
                        "account_not_found", e.getMessage())));
        config.routes.exception(UserNotFoundException.class,
                (e, ctx) ->
                ctx.status(404).json(new ErrorResponse(
                        "user_not_found", e.getMessage())));
        config.routes.exception(TransactionNotFoundException.class,
                (e, ctx) ->
                ctx.status(404).json(new ErrorResponse(
                        "transaction_not_found", e.getMessage())));

        // Auth → 401 / 403
        config.routes.exception(UnauthorizedException.class,
                (e, ctx) ->
                ctx.status(401).json(new ErrorResponse(
                        "unauthorized", e.getMessage())));
        config.routes.exception(ForbiddenException.class,
                (e, ctx) ->
                ctx.status(403).json(new ErrorResponse(
                        "forbidden", e.getMessage())));
        config.routes.exception(AccountLockedException.class,
                (e, ctx) ->
                ctx.status(423).json(new ErrorResponse(
                        "account_locked", e.getMessage())));  // 423 Locked

        // Bad request body (malformed JSON, missing fields, etc.)
        config.routes.exception(io.javalin.http.BadRequestResponse.class,
                (e, ctx) ->
                ctx.status(400).json(new ErrorResponse(
                        "bad_request", e.getMessage())));

        // Catch-all — unexpected runtime errors become 500
        config.routes.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();  // log it; never expose stack traces to client
            ctx.status(500).json(new ErrorResponse(
                    "internal_error", "An unexpected error occurred"));
        });
    }
}
