package com.bank.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bank.adapter.*;
import com.bank.account.Account;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

/*
this class serves only one purpose and that is that i dont want to have to constantly pass
Gson params into the Json repos. this lets me just call this method every time i want it
also ensures that if one of them doesnt exist, the project wont compile */

public class GsonFactory {
    /**
     * @author Derek Homel
     * @summary
     * @return returns a GsonBuilder with all the custom adapter classes
     */
    public static @NotNull Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,
                        new LocalDateTimeAdapter())
                .registerTypeAdapter(UUID.class, new UUIDAdapter())
                .registerTypeAdapter(Account.class, new AccountAdapter())
                .registerTypeAdapter(YearMonth.class, new YearMonthAdapter())
                .create();
    }
}
