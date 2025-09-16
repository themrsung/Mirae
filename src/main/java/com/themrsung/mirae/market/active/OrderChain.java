package com.themrsung.mirae.market.active;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An order chain.
 */
public final class OrderChain implements Serializable {
    /**
     * The recent fulfillment cutoff.
     */
    public static final int RECENT_FULFILLMENT_CUTOFF = 1000;

    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<OrderChain> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<OrderChain> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new order chain.
     */
    public OrderChain() {
        this.recentPrice = Double.NaN;
        this.fulfillmentQueue = new ConcurrentLinkedQueue<>();

        this.buyOrders = Collections.synchronizedList(new ArrayList<>());
        this.sellOrders = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Copy constructor.
     *
     * @param c The other chain
     */
    public OrderChain(@NotNull OrderChain c) {
        this.recentPrice = c.recentPrice;
        this.fulfillmentQueue = new ArrayDeque<>();

        this.buyOrders = Collections.synchronizedList(new ArrayList<>(c.buyOrders));
        this.sellOrders = Collections.synchronizedList(new ArrayList<>(c.sellOrders));

        this.fulfillmentQueue.addAll(c.fulfillmentQueue);
    }

    private double recentPrice;
    private final @NotNull Queue<Fulfillment> fulfillmentQueue;

    private transient final @NotNull List<Order> buyOrders;
    private transient final @NotNull List<Order> sellOrders;

    /**
     * Returns the recent price.
     *
     * @return The recent price
     */
    public double getRecentPrice() {
        return recentPrice;
    }

    /**
     * Returns the list of recent fulfillments.
     *
     * @return The list of recent fulfillments
     */
    public @NotNull List<Fulfillment> getRecentFulfillments() {
        return List.copyOf(fulfillmentQueue);
    }

    /**
     * Returns the recent fulfillment quantity sum.
     *
     * @return The recent fulfillment quantity sum
     */
    public long getRecentFulfillmentQuantity() {
        return fulfillmentQueue.stream().mapToLong(Fulfillment::quantity).sum();
    }

    /**
     * Returns the recent weighted average price.
     *
     * @return The recent weighted average price
     */
    public double getWeightedAveragePrice() {
        double tally = 0;
        long quantity = 0;

        for (Fulfillment f : List.copyOf(fulfillmentQueue)) {
            tally += f.price() * f.quantity();
            quantity += f.quantity();
        }

        if (quantity == 0) return recentPrice;
        return Math.round(tally / quantity);
    }

    /**
     * Checks and returns the current market price.
     *
     * @return The current market price
     */
    public synchronized double getMarketPrice() {
        sortOrders();

        Order firstBuy = buyOrders.getFirst();
        Order firstSell = sellOrders.getFirst();

        if (firstBuy == null) {
            if (firstSell == null) return recentPrice;

            return firstSell.getPriceOrdered();
        } else {
            if (firstSell == null) return firstBuy.getPriceOrdered();

            double buyPrice = firstBuy.getPriceOrdered();
            double sellPrice = firstSell.getPriceOrdered();

            return Math.round((buyPrice + sellPrice) / 2);
        }
    }

    /**
     * Returns the list of buy orders. May not be sorted.
     *
     * @return The list of buy orders
     */
    public @NotNull List<Order> getBuyOrders() {
        return List.copyOf(buyOrders);
    }

    /**
     * Returns the list of sell orders. May not be sorted.
     *
     * @return The list of sell orders
     */
    public @NotNull List<Order> getSellOrders() {
        return List.copyOf(sellOrders);
    }

    /**
     * Sorts the order list.
     */
    public synchronized void sortOrders() {
        buyOrders.sort(Order::buyCompare);
        sellOrders.sort(Order::sellCompare);
    }

    /**
     * Process outstanding orders.
     */
    public synchronized void processOrders() {
        clearFulfilledOrders();
        sortOrders();

        List<Order> buy = new ArrayList<>(buyOrders);
        List<Order> sell = new ArrayList<>(sellOrders);

        buy.forEach(b -> {
            if (b.getQuantityRemaining() <= 0) return;

            sell.forEach(s -> {
                if (b.getQuantityRemaining() <= 0 || s.getQuantityRemaining() <= 0) return;

                OrderType buyType = b.getType();
                OrderType sellType = s.getType();

                boolean buyMarket = buyType.isMarket();
                boolean sellMarket = sellType.isMarket();

                if (buyMarket && sellMarket) {
                    // Market order settlement is not supported yet.
                    return;
                }

                double buyPrice = b.getPriceOrdered();
                double sellPrice = s.getPriceOrdered();

                // Return if there is a limit order and prices are incompatible
                if (!(buyMarket || sellMarket) && buyPrice < sellPrice) return;

                long quantity = Math.min(b.getQuantityRemaining(), s.getQuantityRemaining());
                double price;

                if (buyMarket || sellMarket) {
                    price = buyMarket ? sellPrice : buyPrice;
                } else {
                    price = Math.round((buyPrice + sellPrice) / 2);
                }

                b.onFulfilled(quantity, price);
                s.onFulfilled(quantity, price);

                recentPrice = price;
                fulfillmentQueue.offer(new Fulfillment(quantity, price));

                if (fulfillmentQueue.size() > RECENT_FULFILLMENT_CUTOFF) {
                    fulfillmentQueue.poll();
                }
            });
        });
    }

    /**
     * Places an order.
     *
     * @param order The order
     */
    public synchronized void placeOrder(@NotNull Order order) {
        switch (order.getType()) {
            case BUY_LIMIT, BUY_MARKET -> buyOrders.add(order);
            case SELL_LIMIT, SELL_MARKET -> sellOrders.add(order);
            default -> throw new IllegalArgumentException("Unknown order type \"" + order.getType() + "\".");
        }
    }

    /**
     * Cancels the order.
     *
     * @param order The order to cancel
     * @return {@code true} if it was cancelled
     */
    public synchronized boolean cancelOrder(@NotNull Order order) {
        return buyOrders.remove(order) || sellOrders.remove(order);
    }

    /**
     * Clears fulfilled orders.
     */
    public synchronized void clearFulfilledOrders() {
        buyOrders.removeIf(o -> o.getQuantityRemaining() <= 0);
        sellOrders.removeIf(o -> o.getQuantityRemaining() <= 0);
    }

    /**
     * Clears sender-less (server) orders.
     */
    public synchronized void clearServerOrders() {
        buyOrders.removeIf(o -> !o.hasSender());
        sellOrders.removeIf(o -> !o.hasSender());
    }

    /**
     * Clears orders, but retains those placed after the cutoff time.
     *
     * @param cutoff The cutoff time
     */
    public synchronized void clearOrders(@NotNull LocalDateTime cutoff) {
        buyOrders.removeIf(o -> o.getTime().isBefore(cutoff));
        sellOrders.removeIf(o -> o.getTime().isBefore(cutoff));
    }

    /**
     * Clears the order list.
     */
    public synchronized void clearOrders() {
        buyOrders.clear();
        sellOrders.clear();
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<OrderChain> {
        @Override
        public JsonElement serialize(OrderChain chain, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("recentPrice", new JsonPrimitive(chain.recentPrice));

            JsonArray fulfillments = new JsonArray();
            chain.fulfillmentQueue.forEach(f -> fulfillments.add(context.serialize(f)));

            object.add("fulfillmentStack", fulfillments);

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<OrderChain> {
        @Override
        public OrderChain deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();
            OrderChain chain = new OrderChain();

            if (object.has("recentPrice") && object.get("recentPrice").isJsonPrimitive()) {
                chain.recentPrice = object.get("recentPrice").getAsDouble();
            }

            if (object.has("fulfillmentStack") && object.get("fulfillmentStack").isJsonArray()) {
                JsonArray fulfillments = object.get("fulfillmentStack").getAsJsonArray();
                fulfillments.forEach(f -> chain.fulfillmentQueue.add(context.deserialize(f, Fulfillment.class)));
            }

            return chain;
        }
    }
}
