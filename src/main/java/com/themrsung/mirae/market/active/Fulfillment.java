package com.themrsung.mirae.market.active;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * A fulfillment data class.
 *
 * @param quantity The quantity fulfilled
 * @param price    The price at which the order was fulfilled at
 */
public record Fulfillment(
        long quantity,
        double price
) implements Serializable {
    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<Fulfillment> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<Fulfillment> deserializer() {
        return DESERIALIZER;
    }

    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<Fulfillment> {
        @Override
        public JsonElement serialize(Fulfillment f, Type type, JsonSerializationContext context) {
            return null;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<Fulfillment> {
        @Override
        public Fulfillment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }
}
