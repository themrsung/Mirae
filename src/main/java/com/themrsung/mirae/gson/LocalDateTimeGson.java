package com.themrsung.mirae.gson;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * {@link LocalDateTime} GSON utility.
 */
public final class LocalDateTimeGson {
    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance.
     */
    public static @NotNull JsonSerializer<LocalDateTime> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    public static @NotNull JsonDeserializer<LocalDateTime> deserializer() {
        return DESERIALIZER;
    }

    private static final Serializer SERIALIZER = new Serializer();
    private static final Deserializer DESERIALIZER = new Deserializer();

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
            long millis = dateTime.atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            return new JsonPrimitive(millis);
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || !jsonElement.isJsonPrimitive()) return null;

            long millis = jsonElement.getAsLong();
            return Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private LocalDateTimeGson() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
