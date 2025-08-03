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
     * @param category The category
     */
    public AbstractMarket(@NotNull UUID uniqueId, @NotNull String name, @NotNull ItemStack item, @NotNull MarketCategory category) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.item = item.clone();
        this.category = category;
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
        this.category = m.getCategory();
    }

    private final @NotNull UUID uniqueId;
    private final @NotNull String name;
    private final @NotNull ItemStack item;
    private @NotNull MarketCategory category;

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

    @Override
    public @NotNull MarketCategory getCategory() {
        return category;
    }

    @Override
    public void setCategory(@NotNull MarketCategory category) {
        this.category = category;
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
            object.add("category", context.serialize(market.getCategory()));
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
        protected MarketCategory category = null;

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

            if (!object.has("category") || object.get("category").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"category\".");
            }

            category = context.deserialize(object.get("category"), MarketCategory.class);
        }
    }
}
