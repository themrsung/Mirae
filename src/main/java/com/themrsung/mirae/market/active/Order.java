package com.themrsung.mirae.market.active;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * An order.
 */
public final class Order {
    /**
     * Creates a new player order.
     *
     * @param sender   The player
     * @param type     The type
     * @param quantity The quantity
     * @return The order
     * @throws IllegalArgumentException When the order type is non-market
     */
    public static @NotNull Order player(@NotNull Account sender, @NotNull OrderType type, long quantity) {
        if (!type.isMarket()) {
            throw new IllegalArgumentException("Non-market orders are not supported for players.");
        }

        return new Order(sender, type, quantity, 0);
    }

    /**
     * Creates a new server order.
     *
     * @param type     The type
     * @param quantity The quantity
     * @param price    The price
     * @return The order
     * @throws IllegalArgumentException When the order type is non-limit
     */
    public static @NotNull Order server(@NotNull OrderType type, long quantity, double price) {
        if (!type.isLimit()) {
            throw new IllegalArgumentException("Non-limit orders are not supported for server.");
        }

        return new Order(null, type, quantity, price);
    }

    /**
     * Buy order comparator.
     *
     * @param a The first order
     * @param b The second order
     * @return The result
     */
    public static int buyCompare(@NotNull Order a, @NotNull Order b) {
        int m1 = a.getType().isMarket() ? 1 : 0;
        int m2 = b.getType().isMarket() ? 1 : 0;
        int marketCompare = -Integer.compare(m1, m2);

        if (marketCompare != 0) return marketCompare;

        double p1 = a.getType().isLimit() ? a.priceOrdered : Double.MAX_VALUE;
        double p2 = b.getType().isLimit() ? b.priceOrdered : Double.MAX_VALUE;
        int priceCompare = -Double.compare(p1, p2);

        if (priceCompare != 0) return priceCompare;

        long q1 = a.getQuantityRemaining();
        long q2 = b.getQuantityRemaining();
        int quantityCompare = -Long.compare(q1, q2);

        return quantityCompare;
    }

    /**
     * Sell order comparator.
     *
     * @param a The first order
     * @param b The second order
     * @return The result
     */
    public static int sellCompare(@NotNull Order a, @NotNull Order b) {
        int m1 = a.getType().isMarket() ? 1 : 0;
        int m2 = b.getType().isMarket() ? 1 : 0;
        int marketCompare = -Integer.compare(m1, m2);

        if (marketCompare != 0) return marketCompare;

        double p1 = a.getType().isLimit() ? a.priceOrdered : -Double.MAX_VALUE;
        double p2 = b.getType().isLimit() ? b.priceOrdered : -Double.MAX_VALUE;
        int priceCompare = Double.compare(p1, p2);

        if (priceCompare != 0) return priceCompare;

        long q1 = a.getQuantityRemaining();
        long q2 = b.getQuantityRemaining();
        int quantityCompare = -Long.compare(q1, q2);

        return quantityCompare;
    }

    /**
     * Creates a new order.
     *
     * @param sender   The sender
     * @param type     The type
     * @param quantity The quantity
     * @param price    The price
     */
    private Order(@Nullable Account sender, @NotNull OrderType type, long quantity, double price) {
        this.time = LocalDateTime.now();
        this.senderId = sender != null ? sender.getUniqueId() : null;
        this.type = type;
        this.quantityOrdered = quantity;
        this.priceOrdered = price;
        this.quantityFulfilled = 0;
        this.priceFulfilled = 0;
    }

    private final @NotNull LocalDateTime time;
    private final @Nullable UUID senderId;
    private final @NotNull OrderType type;
    private final double priceOrdered;
    private final long quantityOrdered;
    private double priceFulfilled;
    private long quantityFulfilled;

    /**
     * Returns the time this order was placed.
     *
     * @return The time this order was placed
     */
    public @NotNull LocalDateTime getTime() {
        return time;
    }

    /**
     * Returns the sender of this order.
     *
     * @return The sender if present, {@code null} otherwise
     */
    public @Nullable Account getSender() {
        return senderId != null ? Mirae.getState().getAccount(senderId) : null;
    }

    /**
     * Returns whether this order has a sender.
     *
     * @return {@code true} if it has a sender
     */
    public boolean hasSender() {
        return senderId != null;
    }

    /**
     * Returns the type of this order.
     *
     * @return The type of this order
     */
    public @NotNull OrderType getType() {
        return type;
    }

    /**
     * Returns the price ordered.
     *
     * @return The price ordered
     */
    public double getPriceOrdered() {
        return priceOrdered;
    }

    /**
     * Returns the quantity ordered.
     *
     * @return The quantity ordered
     */
    public long getQuantityOrdered() {
        return quantityOrdered;
    }

    /**
     * Returns the price fulfilled.
     *
     * @return The price fulfilled
     */
    public double getPriceFulfilled() {
        return priceFulfilled;
    }

    /**
     * Returns the quantity fulfilled.
     *
     * @return The quantity fulfilled
     */
    public long getQuantityFulfilled() {
        return quantityFulfilled;
    }

    /**
     * Returns the quantity remaining.
     *
     * @return The quantity remaining
     */
    public long getQuantityRemaining() {
        return quantityOrdered - quantityFulfilled;
    }

    /**
     * Called upon fulfillment.
     *
     * @param quantity The quantity which was fulfilled
     * @param price    The price at which it was fulfilled
     */
    void onFulfilled(long quantity, double price) {
        long quantityBefore = quantityFulfilled;
        double volumeBefore = priceFulfilled * quantityBefore;

        quantityFulfilled += quantity;

        long quantityAfter = quantityFulfilled;
        double volumeAfter = volumeBefore + quantity * price;

        priceFulfilled = quantityAfter != 0 ? volumeAfter / quantityAfter : 0;
    }
}
