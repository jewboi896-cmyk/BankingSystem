package com.bank.api.controller;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public class HealthController {
    /**
     * @author Derek Homel
     * @param ctx the context for the request
     */
    public void health(@NotNull Context ctx) {
        ctx.json(Map.of("status", "ok",
                "timestamp", LocalDateTime.now()));
    }
}
