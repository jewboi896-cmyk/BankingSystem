package com.bank.api;

import com.bank.exception.InvalidRequestException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class APIUtils {
    public static @NotNull UUID parsePathUUID(@NotNull Context ctx, String paramName)
            throws InvalidRequestException {
        try {
            return UUID.fromString(ctx.pathParam(paramName));
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid " + paramName + " format");
        }
    }
}
