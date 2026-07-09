package com.bank.auth.jwt;

import com.bank.role.Role;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RequestContext {
    /**
     * @author Derek Homel
     * @summary
     * @param ctx the context for the request
     * @return returns the userID key from the context
     */
    public static @NotNull UUID userID(@NotNull Context ctx) {
        UUID uid = ctx.attribute("userID");
        if (uid == null) throw new IllegalStateException(
                "No userID in context — middleware not applied?");
        return uid;
    }

    /**
     * @author Derek Homel
     * @summary
     * @param ctx the context for the request
     * @return returns the role key from the context
     */
    public static @NotNull Role role(@NotNull Context ctx) {
        Role role = ctx.attribute("role");
        if (role == null) throw new IllegalStateException(
                "No role in context — middleware not applied?");
        return role;
    }
}
