package com.themrsung.mirae.market.active;

import com.google.gson.*;
import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.market.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * A market where price changes actively.
 */
public final class ActivePriceMarket extends AbstractMarket {
    /**
     * Server order update threshold.
     */
    public static final double SERVER_ORDER_UPDATE_THRESHOLD = 0.7;

    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<ActivePriceMarket> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<ActivePriceMarket> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new active price market.
     *
     * @param name         The name of the market
     * @param item         The item
     * @param category     The category
     * @param defaultPrice The default price
     */
    public ActivePriceMarket(@NotNull String name, @NotNull ItemStack item, @NotNull MarketCategory category, double defaultPrice) {
        super(UUID.randomUUID(), name, item, category);

        this.orderChain = new OrderChain();
        this.volatilityLevel = VolatilityLevel.MODERATE;
        this.defaultPrice = defaultPrice;
    }

    /**
     * Copy constructor.
     *
     * @param m The other market
     */
    public ActivePriceMarket(@NotNull Market m) {
        super(m);

        if (m instanceof ActivePriceMarket apm) {
            this.orderChain = new OrderChain(apm.orderChain);
            this.volatilityLevel = apm.volatilityLevel;
            this.defaultPrice = apm.defaultPrice;
        } else {
            this.orderChain = new OrderChain();
            this.volatilityLevel = VolatilityLevel.MODERATE;
            this.defaultPrice = 0;
        }
    }

    /**
     * Full constructor.
     *
     * @param uniqueId        The unique identifier
     * @param name            The name
     * @param item            The item
     * @param category        The market category
     * @param defaultPrice    The default price
     * @param orderChain      The order chain
     * @param volatilityLevel The volatility level
     */
    private ActivePriceMarket(
            @NotNull UUID uniqueId,
            @NotNull String name,
            @NotNull ItemStack item,
            @NotNull MarketCategory category,
            double defaultPrice,
            @NotNull OrderChain orderChain,
            @NotNull VolatilityLevel volatilityLevel) {
        super(uniqueId, name, item, category);
        this.defaultPrice = defaultPrice;
        this.orderChain = orderChain;
        this.volatilityLevel = volatilityLevel;
    }

    private double defaultPrice;
    private final @NotNull OrderChain orderChain;
    private @NotNull VolatilityLevel volatilityLevel;

    @Override
    public @NotNull MarketType getType() {
        return MarketType.ACTIVE_PRICE;
    }

    /**
     * Returns the default price.
     *
     * @return The default price
     */
    public double getDefaultPrice() {
        return defaultPrice;
    }

    /**
     * Sets the default price.
     *
     * @param defaultPrice The default price
     */
    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    /**
     * Returns the order chain.
     *
     * @return The order chain
     */
    public @NotNull OrderChain getOrderChain() {
        return orderChain;
    }

    /**
     * Returns the volatility level.
     *
     * @return The volatility level
     * @see VolatilityLevel
     */
    public @NotNull VolatilityLevel getVolatilityLevel() {
        return volatilityLevel;
    }

    /**
     * Sets the volatility level.
     *
     * @param volatilityLevel The volatility level
     */
    public void setVolatilityLevel(@NotNull VolatilityLevel volatilityLevel) {
        this.volatilityLevel = volatilityLevel;
    }

    @Override
    public @NotNull PriceQueryResult getBuyPrice(long quantity) {
        orderChain.sortOrders(); // Chain needs sorting anyway. Doing it more often saves resources.

        long remaining = quantity;
        double sumProduct = 0;

        for (Order o : orderChain.getSellOrders()) {
            if (remaining <= 0) break;

            double p = o.getPriceOrdered();
            long q = Math.min(remaining, o.getQuantityRemaining());

            sumProduct += p * q;
            remaining -= q;
        }

        long fulfillable = quantity - remaining;
        double price = fulfillable != 0 ? sumProduct / fulfillable : 0;

        return new PriceQueryResult(this, fulfillable, price);
    }

    @Override
    public @NotNull PriceQueryResult getSellPrice(long quantity) {
        orderChain.sortOrders(); // Chain needs sorting anyway. Doing it more often saves resources.

        long remaining = quantity;
        double sumProduct = 0;

        for (Order o : orderChain.getBuyOrders()) {
            if (remaining <= 0) break;

            double p = o.getPriceOrdered();
            long q = Math.min(remaining, o.getQuantityRemaining());

            sumProduct += p * q;
            remaining -= q;
        }

        long fulfillable = quantity - remaining;
        double price = fulfillable != 0 ? sumProduct / fulfillable : 0;

        return new PriceQueryResult(this, fulfillable, price);
    }

    @Override
    public long getTotalAskQuantity() {
        return orderChain.getSellOrders().stream()
                .mapToLong(Order::getQuantityRemaining)
                .sum();
    }

    @Override
    public long getTotalBidQuantity() {
        return orderChain.getBuyOrders().stream()
                .mapToLong(Order::getQuantityRemaining)
                .sum();
    }

    @Override
    public @NotNull OrderResult buy(@NotNull Account account, @NotNull Inventory delivery, long quantity) {
        PriceQueryResult pqr = getBuyPrice(quantity);

        if (Mirae.getState().isEconomyFrozen()) {
            return new OrderResult(this, account, pqr.quantity(), 0, pqr.price(), 0);
        }

        Order order = Order.player(account, OrderType.BUY_MARKET, pqr.quantity());
        orderChain.placeOrder(order);
        orderChain.processOrders();
        orderChain.cancelOrder(order);

        long quantityFulfilled = order.getQuantityFulfilled();
        double priceFulfilled = order.getPriceFulfilled();

        double volume = Math.abs(quantityFulfilled * priceFulfilled);
        double fees = volume * Markets.getActiveFeeRateFor(account);

        double amountToWithdraw = Math.ceil(quantityFulfilled * priceFulfilled + fees);
        account.modifyBalance(-amountToWithdraw, EconomyCause.MARKET_TRANSACTION_BUY, "Bought items from market.");

        ItemStack items = getItem();
        items.setAmount((int) quantityFulfilled);

        MX.giveItems(delivery, items);

        return new OrderResult(this, account, pqr.quantity(), quantityFulfilled, pqr.price(), priceFulfilled);
    }

    @Override
    public @NotNull OrderResult sell(@NotNull Account account, @NotNull Inventory delivery, long quantity) {
        PriceQueryResult pqr = getSellPrice(quantity);

        if (Mirae.getState().isEconomyFrozen()) {
            return new OrderResult(this, account, pqr.quantity(), 0, pqr.price(), 0);
        }

        Order order = Order.player(account, OrderType.SELL_MARKET, pqr.quantity());
        orderChain.placeOrder(order);
        orderChain.processOrders();
        orderChain.cancelOrder(order);

        long quantityFulfilled = order.getQuantityFulfilled();
        double priceFulfilled = order.getPriceFulfilled();

        double volume = Math.abs(quantityFulfilled * priceFulfilled);
        double fees = volume * Markets.getActiveFeeRateFor(account);

        double amountToDeposit = Math.floor(quantityFulfilled * priceFulfilled - fees);
        account.modifyBalance(amountToDeposit, EconomyCause.MARKET_TRANSACTION_SELL, "Sold items to market.");

        ItemStack items = getItem();
        items.setAmount((int) quantityFulfilled);

        MX.takeItems(delivery, items);

        return new OrderResult(this, account, pqr.quantity(), quantityFulfilled, pqr.price(), priceFulfilled);
    }

    /**
     * Updates server orders.
     */
    public void updateServerOrders() {
        long existingBuy = orderChain.getBuyOrders().stream()
                .filter(o -> !o.hasSender())
                .mapToLong(Order::getQuantityRemaining)
                .sum();

        long existingSell = orderChain.getSellOrders().stream()
                .filter(o -> !o.hasSender())
                .mapToLong(Order::getQuantityRemaining)
                .sum();

        long existingOrders = existingBuy + existingSell;
        double remainingOrderRatio = (double) existingOrders / (double) volatilityLevel.getTotalOrderCount();
        if (remainingOrderRatio > SERVER_ORDER_UPDATE_THRESHOLD || existingOrders > volatilityLevel.getTotalOrderCount())
            return;

        boolean marketBuying = existingSell >= existingBuy;

        double averageRaw = orderChain.getWeightedAveragePrice();
        boolean hasAverage = Double.isFinite(averageRaw);
        double average = hasAverage ? averageRaw : defaultPrice;

        double tickSize = Markets.getTickSizeAt(average);
        double basePrice = Markets.snapToNearestTick(hasAverage ? average : defaultPrice + (marketBuying ? tickSize : 0));

        orderChain.clearServerOrders();

        int numSteps = volatilityLevel.getNumSteps();
        long quantityPerStep = volatilityLevel.getQuantityPerStep();

        // Place buy orders
        double buyPrice = basePrice - Markets.getTickSizeAt(basePrice);
        for (int i = 0; i < numSteps; i++) {
            double p = buyPrice;
            buyPrice -= Markets.getTickSizeAt(buyPrice);

            Order order = Order.server(OrderType.BUY_LIMIT, quantityPerStep, p);
            orderChain.placeOrder(order);
        }

        // Place sell orders
        double sellPrice = basePrice;
        for (int i = 0; i < numSteps; i++) {
            double p = sellPrice;
            sellPrice += Markets.getTickSizeAt(sellPrice);
            Order order = Order.server(OrderType.SELL_LIMIT, quantityPerStep, p);
            orderChain.placeOrder(order);
        }

        orderChain.processOrders();
    }

    /**
     * Serializer class.
     */
    private static final class Serializer extends AbstractMarket.Serializer<ActivePriceMarket> {
        @Override
        public JsonElement serialize(ActivePriceMarket market, Type type, JsonSerializationContext context) {
            JsonElement element = super.serialize(market, type, context);
            JsonObject object = element.getAsJsonObject();

            object.add("defaultPrice", new JsonPrimitive(market.defaultPrice));
            object.add("orderChain", context.serialize(market.orderChain));
            object.add("volatilityLevel", context.serialize(market.volatilityLevel));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer extends AbstractMarket.Deserializer<ActivePriceMarket> {
        @Override
        public ActivePriceMarket deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                initialize(jsonElement, type, context);
            } catch (NullPointerException ignored) {
                return null;
            } catch (JsonParseException e) {
                throw new JsonParseException("Error deserializing \"AbstractMarket\".", e);
            }

            JsonObject object = jsonElement.getAsJsonObject();

            double defaultPrice = 0;
            OrderChain orderChain = new OrderChain();
            VolatilityLevel volatilityLevel = null;

            if (!object.has("defaultPrice") || !object.get("defaultPrice").isJsonPrimitive()) {
                throw new JsonParseException("Missing required parameter \"defaultPrice\".");
            }

            defaultPrice = object.get("defaultPrice").getAsDouble();

            if (!object.has("orderChain") || object.get("orderChain").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"orderChain\".");
            }

            orderChain = context.deserialize(object.get("orderChain"), OrderChain.class);

            if (!object.has("volatilityLevel") || object.get("volatilityLevel").isJsonNull()) {
                throw new JsonParseException("Missing required parameter \"volatilityLevel\".");
            }

            volatilityLevel = context.deserialize(object.get("volatilityLevel"), VolatilityLevel.class);


            return new ActivePriceMarket(uniqueId, name, item, category, defaultPrice, orderChain, volatilityLevel);
        }
    }
}
