package com.bank.adapter;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,
        JsonDeserializer<LocalDateTime> {
    // sets formatter to use local date time no matter location
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * @summary
     * @author Derek Homel
     * @param src the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context the context of the serialized object
     * @return returns a JsonPrimitive of a DateTimeFormater as a string
     */
    @Override
    public @NotNull JsonElement serialize(@NotNull LocalDateTime src,
                                          Type typeOfSrc,
                                          JsonSerializationContext context) {
        return new JsonPrimitive(src.format(FORMATTER));
    }

    /**
     * @summary
     * @author Derek Homel
     * @param json The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context the context of the deserialized object
     * @return returns an instance of LocalDateTime from the json string and a
     * TimeDateFormatter
     * @throws JsonParseException throws if json cannot be parsed
     */
    @Override
    public @NotNull LocalDateTime deserialize(@NotNull JsonElement json,
                                              Type typeOfT,
                                              JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), FORMATTER);
    }
}
