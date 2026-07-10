package com.bank.auth.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bank.exception.UnauthorizedException;
import com.bank.role.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AuthMiddleware implements Handler {
    private final JWTService jwtService;

    public AuthMiddleware(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * @author Derek Homel
     * @summary
     * @param ctx the current request's context. Use this to process the
     *            request's parameter (query, form params, body, …)
     *            and build up the response (status code, payload, …)
     * @throws UnauthorizedException throws if token isnt authorized
     */
    @Override
    public void handle(@NotNull Context ctx) throws UnauthorizedException {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed Authorization header");
        }
        String token = authHeader.substring(7);  // strip "Bearer "
        try {
            DecodedJWT decoded = jwtService.verifyToken(token);
            ctx.attribute("userID", UUID.fromString(decoded.getSubject()));
            ctx.attribute("role", Role.valueOf(decoded.getClaim("role")
                    .asString()));
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
