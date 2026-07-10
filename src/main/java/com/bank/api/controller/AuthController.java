package com.bank.api.controller;

import com.bank.api.dto.request.ChangePasswordRequest;
import com.bank.api.dto.request.LoginRequest;
import com.bank.api.dto.request.RegisterRequest;
import com.bank.api.dto.response.LoginResponse;
import com.bank.api.dto.response.UserResponse;
import com.bank.auth.AuthService;
import com.bank.auth.jwt.JWTService;
import com.bank.auth.jwt.RequestContext;
import com.bank.exception.BankingException;
import com.bank.user.User;
import io.javalin.http.Context;

import java.util.UUID;

public class AuthController {
    AuthService authService;
    JWTService jwtService;
    private final int expiryTimeInSeconds = 900;

    public AuthController(AuthService authService, JWTService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void register(Context ctx) throws BankingException {
        RegisterRequest rReq = ctx.bodyAsClass(RegisterRequest.class);
        User user = authService.registerUser(rReq.username(), rReq.firstName(),
                rReq.lastName(), rReq.middleInitial(), rReq.password(), rReq.role());
        ctx.status(201).json(new UserResponse(user.getUserID(),
                user.getUsername(), user.getRole()));
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void login(Context ctx) throws BankingException {
        LoginRequest lReq = ctx.bodyAsClass(LoginRequest.class);
        User user = authService.login(lReq.username(), lReq.password());
        String token = jwtService.generateToken(user);
        ctx.json(new LoginResponse(token, user.getUserID(),
                user.getUsername(), user.getRole(), expiryTimeInSeconds));
    }

    /**
     * @author Derek Homel
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void changePassword(Context ctx) throws BankingException {
        UUID userID = RequestContext.userID(ctx);
        ChangePasswordRequest cPReq = ctx.bodyAsClass(ChangePasswordRequest.class);
        authService.changePassword(userID, cPReq.currentPassword(),
                cPReq.newPassword());
        // 204 -> success, no content
        ctx.status(204);
    }
}
