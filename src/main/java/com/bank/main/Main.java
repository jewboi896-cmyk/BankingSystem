package com.bank.main;

import com.bank.account.AccountService;
import com.bank.api.AccountOwnershipGuard;
import com.bank.api.ExceptionMapper;
import com.bank.api.RouteConfig;
import com.bank.api.controller.*;
import com.bank.auth.AuthService;
import com.bank.auth.jwt.AuthMiddleware;
import com.bank.auth.jwt.JWTService;
import com.bank.exception.*;
import com.bank.repository.AccountRepository;
import com.bank.repository.RepoFactory;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.transaction.TransactionService;
import com.bank.user.UserService;
import io.javalin.Javalin;

public class Main {
    private final static int SECRET_LENGTH = 32;
    public static void main(String[] args) {
        String secret = System.getenv("BANK_JWT_SECRET");
        if ((secret == null) || (secret.length() < SECRET_LENGTH)) {
            System.err.println("FATAL: BANK_JWT_SECRET environment variable must be set " +
                    "and at least 32 characters long.");
            System.exit(1);
        }
        // init in mem repos
        UserRepository userRepo = RepoFactory.createUserRepo();
        AccountRepository accountRepo = RepoFactory.createAccountRepo();
        TransactionRepository transactionRepo = RepoFactory.createTransactionRepo();

        // init services
        AuthService authService = new AuthService(userRepo);
        UserService userService = new UserService(userRepo);
        AccountService accountService = new AccountService(accountRepo, userRepo);
        TransactionService transactionService = new TransactionService(transactionRepo, accountService);
        JWTService jwtService = new JWTService(secret);

        // init auth
        AuthMiddleware authMiddleware = new AuthMiddleware(jwtService);
        AccountOwnershipGuard guard = new AccountOwnershipGuard(accountService);

        // init controllers
        AuthController authController = new AuthController(authService, jwtService);
        AccountController accountController = new AccountController(accountService, guard);
        TransactionController transactionController = new TransactionController(transactionService, guard);
        HealthController healthController = new HealthController();
        UserController userController = new UserController(userService);

        // init Javalin server
        Javalin.create(config -> {
            // init routes
            RouteConfig.registerRoutes(config, authMiddleware, authController,
                    accountController, transactionController,
                    healthController, userController);

            // init global exception handler
            ExceptionMapper.registerExceptionHandlers(config);

        }).start(7070);
    }
}
