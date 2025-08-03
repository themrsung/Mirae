package com.themrsung.mirae.market;

import com.google.gson.*;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.market.active.ActivePriceMarket;
import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * A market.
 */
public interface Market extends Serializable {
    /**
     * Fee rate for active markets.
     */
    double ACTIVE_MARKET_FEE_RATE = 0.00015;

    /**
     * Fee rate for fixed markets.
     */
    double FIXED_MARKET_FEE_RATE = 0.0;

    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    static @NotNull Serializer serializer() {
        return Serializer.SERIALIZER;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    static @NotNull Deserializer deserializer() {
        return Deserializer.DESERIALIZER;
    }

    /**
     * Returns the unique identifier of this market.
     *
     * @return The unique identifier
     */
    @NotNull UUID getUniqueId();

    /**
     * Returns the name of this market.
     *
     * @return The name of this market
     */
    @NotNull String getName();

    /**
     * Returns the type of this market.
     *
     * @return The type of this market
     */
    @NotNull MarketType getType();

    /**
     * Returns the category of this market.
     *
     * @return The category of this market
     */
    @NotNull MarketCategory getCategory();

    /**
     * Sets the category of this market.
     *
     * @param category The category
     */
    void setCategory(@NotNull MarketCategory category);

    /**
     * Returns the item which is being traded.
     *
     * @return The item which is being traded
     */
    @NotNull ItemStack getItem();

    /**
     * Returns the buy price.
     *
     * @param quantity The quantity the buyer wishes to buy
     * @return The price query result
     */
    @NotNull PriceQueryResult getBuyPrice(long quantity);

    /**
     * Returns the sell price.
     *
     * @param quantity The quantity the seller wishes to buy
     * @return The price query result
     */
    @NotNull PriceQueryResult getSellPrice(long quantity);

    /**
     * Returns the total ask quantity.
     *
     * @return The total ask quantity
     */
    long getTotalAskQuantity();

    /**
     * Returns the total bid quantity.
     *
     * @return The total bid quantity
     */
    long getTotalBidQuantity();

    /**
     * Buys items from this market.
     *
     * @param account  The account which is buying
     * @param delivery The delivery inventory
     * @param quantity The quantity
     * @return The result
     */
    @NotNull OrderResult buy(@NotNull Account account, @NotNull Inventory delivery, long quantity);

    /**
     * Sells items to this market.
     *
     * @param account  The account which is selling
     * @param delivery The delivery inventory
     * @param quantity The quantity
     * @return The result
     */
    @NotNull OrderResult sell(@NotNull Account account, @NotNull Inventory delivery, long quantity);

    /**
     * Serializer class.
     */
    class Serializer implements JsonSerializer<Market> {
        private static final @NotNull Serializer SERIALIZER = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(Market market, Type type, JsonSerializationContext context) {
            if (market == null) return JsonNull.INSTANCE;

            return switch (market.getType()) {
                case ACTIVE_PRICE ->
                        ActivePriceMarket.serializer().serialize((ActivePriceMarket) market, type, context);
                case FIXED_PRICE -> FixedPriceMarket.serializer().serialize((FixedPriceMarket) market, type, context);
                default ->
                        throw new JsonParseException("Cannot serialize unknown implementation \"" + market.getClass().getSimpleName() + "\".");
            };
        }
    }

    /**
     * Deserializer class.
     */
    class Deserializer implements JsonDeserializer<Market> {
        private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

        private Deserializer() {
        }

        @Override
        public Market deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) {
                return null;
            }

            JsonObject object = jsonElement.getAsJsonObject();
            if (!object.has("type") || object.get("type").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"type\".");
            }

            MarketType marketType = context.deserialize(object.get("type"), MarketType.class);

            return switch (marketType) {
                case ACTIVE_PRICE -> ActivePriceMarket.deserializer().deserialize(jsonElement, type, context);
                case FIXED_PRICE -> FixedPriceMarket.deserializer().deserialize(jsonElement, type, context);
                default -> null;
            };
        }
    }
}
