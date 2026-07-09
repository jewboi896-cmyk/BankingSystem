package com.bank.adapter;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDAdapter implements JsonSerializer<UUID>,
        JsonDeserializer<UUID> {

    /**
     * @summary
     * @author Derek Homel
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context the context of the serialized object
     * @return returns a JsonPrimitive of the UUID as a string
     */
    @Override
    public @NotNull JsonElement serialize(UUID src, Type typeOfSrc,
                                          JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    /**
     * @summary
     * @author Derek Homel
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context the context of the deserialized object
     * @return returns a UUID as a string
     * @throws JsonParseException throws when json cannot be parsed
     */
    @Override
    public @NotNull UUID deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context)
            throws JsonParseException {
        return UUID.fromString(json.getAsString());
    }
}
