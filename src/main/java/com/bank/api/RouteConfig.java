package com.bank.api;

import com.bank.api.controller.*;
import com.bank.auth.jwt.AuthMiddleware;
import io.javalin.config.JavalinConfig;
import org.jetbrains.annotations.NotNull;

public class RouteConfig {
    /**
     * @author Derek Homel
     * @summary
     * @param config the Javalin config
     * @param auth the AuthMiddleware object
     * @param authC the AuthController object
     * @param accountC the AccountController object
     * @param txC the TransactionController object
     * @param healthC the HealthController object
     * @param uCont the UserController object
     */
    public static void registerRoutes(@NotNull JavalinConfig config,
                                      AuthMiddleware auth,
                                      @NotNull AuthController authC,
                                      @NotNull AccountController accountC,
                                      @NotNull TransactionController txC,
                                      @NotNull HealthController healthC,
                                      @NotNull UserController uCont) {
        // Public
        config.routes.post("/api/auth/register", authC::register);
        config.routes.post("/api/auth/login", authC::login);
        config.routes.get("/api/health", healthC::health);

        // Protected — apply middleware via before-handler matching path prefix
        config.routes.before("/api/auth/change-password", auth);
        config.routes.before("/api/accounts/*", auth);

        config.routes.post("/api/auth/change-password", authC::changePassword);

        config.routes.post("/api/accounts/checking", accountC::createChecking);
        config.routes.post("/api/accounts/savings", accountC::createSavings);
        config.routes.get("/api/accounts", accountC::listMyAccounts);
        config.routes.get("/api/accounts/{id}", accountC::getAccount);

        config.routes.post("/api/accounts/{id}/deposit", txC::deposit);
        config.routes.post("/api/accounts/{id}/withdraw", txC::withdraw);
        config.routes.post("/api/accounts/transfer", txC::transfer);
        config.routes.get("/api/accounts/{id}/transactions", txC::getHistory);

        config.routes.before("/api/users/*", auth);
        config.routes.get("/api/users/me", uCont::getUser);

    }
}
