package com.bank.adapter;

import com.bank.account.Account;
import com.bank.account.CheckingAccount;
import com.bank.account.SavingsAccount;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;

public class AccountAdapter implements JsonSerializer<Account>,
        JsonDeserializer<Account> {

    private static final String TYPE_DISCRIMINATOR = "_type";

    /**
     * @author Derek Homel
     * @summary
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context the context of the source object to be serialized
     * @return returns the serialized object
     */
    @Override
    public @NotNull JsonElement serialize(Account src, Type typeOfSrc,
                                          @NotNull JsonSerializationContext context) {
        JsonObject object = context.serialize(src, src.getClass())
                .getAsJsonObject();
        object.addProperty(TYPE_DISCRIMINATOR, src.getAccountType().name());
        return object;
    }

    /**
     * @summary
     * @author Derek Homel
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context context of the source object to be deserialized
     * @return switches on the specific account subclass and returns the
     * deserialized object based upon that (Checking vs Savings)
     * @throws JsonParseException throws when a json string cannot be parsed
     */
    @Override
    public @NotNull Account deserialize(@NotNull JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        String type = object.get(TYPE_DISCRIMINATOR).getAsString();
        /* need to ensure that Gson knows which type of account to create since
        Account class is abstract:
        note to add investment account here once implemented */
        return switch (type) {
            case "CHECKING" -> context.deserialize(object,
                    CheckingAccount.class);
            case "SAVINGS"  -> context.deserialize(object,
                    SavingsAccount.class);
            default -> throw new JsonParseException("Unknown account type: " +
                    type);
        };
    }
}
