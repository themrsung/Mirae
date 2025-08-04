package com.themrsung.mirae.market.fixed;

import com.google.gson.*;
import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.exception.NegativeBidAskSpreadException;
import com.themrsung.mirae.market.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Fixed price market.
 */
public final class FixedPriceMarket extends AbstractMarket {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<FixedPriceMarket> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<FixedPriceMarket> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new fixed price market.
     *
     * @param name     The name of the market
     * @param item     The item
     * @param category The category
     */
    public FixedPriceMarket(@NotNull String name, @NotNull ItemStack item, @NotNull MarketCategory category) {
        super(UUID.randomUUID(), name, item, category);

        this.buyPrice = 0;
        this.sellPrice = 0;

        requireValidSpread();
    }

    /**
     * Copy constructor.
     *
     * @param m The other market
     */
    public FixedPriceMarket(@NotNull Market m) {
        super(m);

        if (m instanceof FixedPriceMarket fpm) {
            this.buyPrice = fpm.buyPrice;
            this.sellPrice = fpm.sellPrice;
        } else {
            this.buyPrice = 0;
            this.sellPrice = 0;
        }

        requireValidSpread();
    }

    /**
     * Full constructor.
     *
     * @param uniqueId  The unique identifier
     * @param name      The name
     * @param item      The item
     * @param category  The category
     * @param buyPrice  The buy price
     * @param sellPrice The sell price
     */
    private FixedPriceMarket(
            @NotNull UUID uniqueId,
            @NotNull String name,
            @NotNull ItemStack item,
            @NotNull MarketCategory category,
            double buyPrice,
            double sellPrice
    ) {
        super(uniqueId, name, item, category);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;

        requireValidSpread();
    }

    private double buyPrice;
    private double sellPrice;

    @Override
    public @NotNull PriceQueryResult getBuyPrice(long quantity) {
        return new PriceQueryResult(this, quantity, buyPrice);
    }

    @Override
    public @NotNull PriceQueryResult getSellPrice(long quantity) {
        return new PriceQueryResult(this, quantity, sellPrice);
    }

    /**
     * Returns whether buying is allowed.
     *
     * @return {@code true} if allowed
     */
    public boolean canBuy() {
        return buyPrice >= 0;
    }

    /**
     * Returns whether selling is allowed.
     *
     * @return {@code true} if allowed
     */
    public boolean canSell() {
        return sellPrice >= 0;
    }

    /**
     * Sets the buy price.
     *
     * @param buyPrice The buy price
     */
    public void setBuyPrice(double buyPrice) {
        double previous = this.buyPrice;

        this.buyPrice = buyPrice;

        try {
            requireValidSpread();
        } catch (NegativeBidAskSpreadException e) {
            Mirae.getInstance().getLogger().warning("Illegal buy price attempted!");
            this.buyPrice = previous;
        }
    }

    /**
     * Sets the sell price.
     *
     * @param sellPrice The sell price
     */
    public void setSellPrice(double sellPrice) {
        double previous = this.sellPrice;

        this.sellPrice = sellPrice;

        try {
            requireValidSpread();
        } catch (NegativeBidAskSpreadException e) {
            Mirae.getInstance().getLogger().warning("Illegal buy price attempted!");
            this.sellPrice = previous;
        }
    }

    @Override
    public @NotNull MarketType getType() {
        return MarketType.FIXED_PRICE;
    }

    @Override
    public long getTotalAskQuantity() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getTotalBidQuantity() {
        return Long.MAX_VALUE;
    }

    @Override
    public @NotNull OrderResult buy(@NotNull Account account, @NotNull Inventory delivery, long quantity) {
        if (buyPrice < 0 || Mirae.getState().isEconomyFrozen()) {
            return new OrderResult(this, account, quantity, 0, 0, 0);
        }

        double amountToWithdraw = Math.ceil(buyPrice * quantity * (1 + FIXED_MARKET_FEE_RATE));
        account.modifyBalance(-amountToWithdraw, EconomyCause.MARKET_TRANSACTION_BUY, "Bought items from market.");

        ItemStack items = getItem();
        items.setAmount((int) quantity);

        MX.giveItems(delivery, items);

        return new OrderResult(this, account, quantity, quantity, buyPrice, buyPrice);
    }

    @Override
    public @NotNull OrderResult sell(@NotNull Account account, @NotNull Inventory delivery, long quantity) {
        if (sellPrice < 0 || Mirae.getState().isEconomyFrozen()) {
            return new OrderResult(this, account, quantity, 0, 0, 0);
        }

        double amountToDeposit = Math.floor(sellPrice * quantity * (1 - FIXED_MARKET_FEE_RATE));
        account.modifyBalance(-amountToDeposit, EconomyCause.MARKET_TRANSACTION_SELL, "Sold items to market.");

        ItemStack items = getItem();
        items.setAmount((int) quantity);

        MX.takeItems(delivery, items);

        return new OrderResult(this, account, quantity, quantity, sellPrice, sellPrice);
    }

    /**
     * Requires that this market's prices are valid.
     *
     * @return The market ({@code this})
     * @throws NegativeBidAskSpreadException When the bid-ask spread is negative
     */
    public @NotNull FixedPriceMarket requireValidSpread() throws NegativeBidAskSpreadException {
        if (buyPrice < 0 || sellPrice < 0) return this; // Either buy or sell is disabled

        boolean valid = buyPrice >= sellPrice;
        if (!valid) throw new NegativeBidAskSpreadException(this, buyPrice, sellPrice);

        return this;
    }

    /**
     * Serializer class.
     */
    private static final class Serializer extends AbstractMarket.Serializer<FixedPriceMarket> {
        @Override
        public JsonElement serialize(FixedPriceMarket market, Type type, JsonSerializationContext context) {
            JsonElement element = super.serialize(market, type, context);
            JsonObject object = element.getAsJsonObject();

            object.add("buyPrice", new JsonPrimitive(market.buyPrice));
            object.add("sellPrice", new JsonPrimitive(market.sellPrice));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer extends AbstractMarket.Deserializer<FixedPriceMarket> {
        @Override
        public FixedPriceMarket deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                initialize(jsonElement, type, context);
            } catch (NullPointerException ignored) {
                return null;
            } catch (JsonParseException e) {
                throw new JsonParseException("Error deserializing \"AbstractMarket\".", e);
            }

            JsonObject object = jsonElement.getAsJsonObject();

            double buyPrice = 0;
            double sellPrice = 0;

            if (!object.has("buyPrice") || !object.get("buyPrice").isJsonPrimitive()) {
                throw new JsonParseException("Missing required parameter \"buyPrice\".");
            }

            buyPrice = object.get("buyPrice").getAsDouble();

            if (!object.has("sellPrice") || !object.get("sellPrice").isJsonPrimitive()) {
                throw new JsonParseException("Missing required parameter \"sellPrice\".");
            }

            sellPrice = object.get("sellPrice").getAsDouble();

            return new FixedPriceMarket(uniqueId, name, item, category, buyPrice, sellPrice);
        }
    }
}
