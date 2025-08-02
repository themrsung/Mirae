package com.themrsung.mirae.gson;

import com.google.gson.*;
import com.themrsung.mirae.util.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * String-Coordinate pair.
 */
public final class StringCoordinatePair implements Serializable {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     * @return The serializer
     */
    public static @NotNull JsonSerializer<StringCoordinatePair> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<StringCoordinatePair> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new pair.
     * @param key The key
     * @param value The value
     */
    public StringCoordinatePair(@NotNull String key, @NotNull Coordinate value) {
        this.key = key;
        this.value = value;
    }

    private final @NotNull String key;
    private final @NotNull Coordinate value;

    /**
     * Returns the key.
     * @return The key
     */
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Returns the value.
     * @return The value
     */
    public @NotNull Coordinate getValue() {
        return value;
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<StringCoordinatePair> {
        @Override
        public JsonElement serialize(StringCoordinatePair pair, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("key", new JsonPrimitive(pair.key));
            object.add("value", context.serialize(pair.value));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<StringCoordinatePair> {
        @Override
        public StringCoordinatePair deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();

            String key = null;
            Coordinate value = null;

            if (!object.has("key") || object.get("key").isJsonNull()) {
                throw new JsonParseException("Missing or invalid parameter \"key\"");
            }

            key = object.get("key").getAsString();

            if (!object.has("value") || object.get("value").isJsonNull()) {
                throw new JsonParseException("Missing or invalid parameter \"value\"");
            }

            value = context.deserialize(object.get("value"), Coordinate.class);

            return new StringCoordinatePair(key, value);
        }
    }
}
