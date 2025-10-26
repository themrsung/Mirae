package com.themrsung.mirae.gson;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * ItemStack-long pair for serialization purposes.
 */
public final class ItemStackLongPair implements Serializable {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<ItemStackLongPair> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<ItemStackLongPair> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new pair instance.
     *
     * @param key   The item stack key
     * @param value The quantity value
     */
    public ItemStackLongPair(@NotNull ItemStack key, long value) {
        this.key = key;
        this.value = value;
    }

    private final @NotNull ItemStack key;
    private final long value;

    /**
     * Returns the key.
     *
     * @return The key
     */
    public @NotNull ItemStack getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    public long getValue() {
        return value;
    }

    private static final class Serializer implements JsonSerializer<ItemStackLongPair> {
        @Override
        public JsonElement serialize(ItemStackLongPair pair, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("key", context.serialize(pair.key));
            object.addProperty("value", pair.value);
            return object;
        }
    }

    private static final class Deserializer implements JsonDeserializer<ItemStackLongPair> {
        @Override
        public ItemStackLongPair deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) {
                return null;
            }

            JsonObject object = jsonElement.getAsJsonObject();

            if (!object.has("key") || object.get("key").isJsonNull()) {
                throw new JsonParseException("Missing parameter \"key\"");
            }
            ItemStack key = context.deserialize(object.get("key"), ItemStack.class);

            if (!object.has("value") || object.get("value").isJsonNull()) {
                throw new JsonParseException("Missing parameter \"value\"");
            }
            long value = object.get("value").getAsLong();

            return new ItemStackLongPair(key, value);
        }
    }
}
