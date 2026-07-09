package com.bank.adapter;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class YearMonthAdapter implements JsonSerializer<YearMonth>,
        JsonDeserializer<YearMonth> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.
            ofPattern("yyyy-MM");

    /**
     * @summary
     * @author Derek Homel
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context the context of the serialized object
     * @return returns a JsonPrimitive in the format of "yyyy-MM"
     */
    @Override
    public @NotNull JsonElement serialize(YearMonth src, Type typeOfSrc,
                                          JsonSerializationContext context) {
        return new JsonPrimitive(src.format(FORMATTER));
    }

    /**
     * @summary
     * @author Derek Homel
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context the context of the deserialized object
     * @return returns the json as a string using yyyy-MM format
     * @throws JsonParseException throws if the json cannot be parsed correctly
     */
    @Override
    public @NotNull YearMonth deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context)
            throws JsonParseException {
        return YearMonth.parse(json.getAsString(), FORMATTER);
    }
}
