package com.bank.api.controller;

import com.bank.api.dto.response.UserResponse;
import com.bank.auth.jwt.RequestContext;
import com.bank.exception.BankingException;
import com.bank.user.User;
import com.bank.user.UserService;
import io.javalin.http.Context;

import java.util.UUID;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @author Derek Homel
     * @summary
     * @param ctx the context for the request
     * @throws BankingException throws a blanket exception so as not to pollute
     * method signature and also because other parts of the codebase will rely
     * on these methods
     */
    public void getUser(Context ctx) throws BankingException {
        UUID userID = RequestContext.userID(ctx);
        User user = userService.getUserByID(userID);
        ctx.json(UserResponse.fromUser(user));
    }
}
