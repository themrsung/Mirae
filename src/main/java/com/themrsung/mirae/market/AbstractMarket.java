package com.themrsung.mirae.market;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Abstract market superclass.
 */
public abstract class AbstractMarket implements Market {
    /**
     * Creates a new abstract market.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     * @param item     The item
     */
    public AbstractMarket(@NotNull UUID uniqueId, @NotNull String name, @NotNull ItemStack item) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.item = item.clone();
    }

    /**
     * Creates a new abstract market.
     *
     * @param m The other market
     */
    public AbstractMarket(@NotNull Market m) {
        this.uniqueId = m.getUniqueId();
        this.name = m.getName();
        this.item = m.getItem().clone();
    }

    private final @NotNull UUID uniqueId;
    private final @NotNull String name;
    private final @NotNull ItemStack item;

    @Override
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item.clone();
    }

    /**
     * Serializer class.
     *
     * @param <T> The type
     */
    protected static abstract class Serializer<T extends AbstractMarket> implements JsonSerializer<T> {
        @Override
        public JsonElement serialize(T market, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("uniqueId", context.serialize(market.getUniqueId()));
            object.add("name", context.serialize(market.getName()));
            object.add("item", context.serialize(market.getItem()));
            object.add("type", context.serialize(market.getType()));

            return object;
        }
    }

    /**
     * Deserializer class.
     *
     * @param <T> The type
     */
    protected static abstract class Deserializer<T extends AbstractMarket> implements JsonDeserializer<T> {
        protected UUID uniqueId = null;
        protected String name = null;
        protected ItemStack item = null;
        protected MarketType marketType = null;

        @Override
        public abstract T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException;

        /**
         * Initializes variables. All variables should be non-null after this is executed.
         *
         * @param jsonElement The JSON element
         * @param type        The type
         * @param context     The deserialization context
         * @throws NullPointerException When the element is {@code null}
         * @throws JsonParseException   When parsing fails
         */
        protected void initialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) {
                throw new NullPointerException("Cannot deserialize null.");
            }

            JsonObject object = jsonElement.getAsJsonObject();

            UUID uniqueId = null;
            String name = null;
            ItemStack item = null;
            MarketType marketType = null;

            if (!object.has("type") || object.get("type").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"type\".");
            }

            marketType = context.deserialize(object.get("type"), MarketType.class);
            if (!AbstractMarket.class.isAssignableFrom(marketType.getImplementation())) {
                throw new JsonParseException("The provided type is not a subtype of \"AbstractMarket\".");
            }

            if (!object.has("uniqueId") || object.get("uniqueId").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"uniqueId\".");
            }

            uniqueId = context.deserialize(object.get("uniqueId"), UUID.class);

            if (!object.has("name") || object.get("name").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"name\".");
            }

            name = object.get("name").getAsString();

            if (!object.has("item") || object.get("item").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"item\"");
            }

            item = context.deserialize(object.get("item"), ItemStack.class);
        }
    }
}
