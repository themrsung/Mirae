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
        return sellCompare(b, a);
    }

    /**
     * Sell order comparator.
     *
     * @param a The first order
     * @param b The second order
     * @return The result
     */
    public static int sellCompare(@NotNull Order a, @NotNull Order b) {
        // Compare market
        int marketCompare = marketCompare(a, b);
        if (marketCompare != 0) return marketCompare;

        // Compare price
        int priceCompare = Double.compare(a.priceOrdered, b.priceOrdered);
        if (priceCompare != 0) return priceCompare; // Price is equal. Incomparable yet.

        int quantityCompare = Long.compare(a.getQuantityRemaining(), b.getQuantityRemaining());
        if (quantityCompare != 0) return quantityCompare; // Quantity is equal. Incomparable yet.

        // Prefer player orders over server orders.
        if (a.hasSender() && b.hasSender()) {
            return 0;
        } else if (a.hasSender()) {
            return 1;
        } else {
            return -1;
        }
    }

    private static int marketCompare(@NotNull Order a, @NotNull Order b) {
        int marketCompare;
        if (a.type.isMarket() && b.type.isMarket()) {
            // Both orders are market; Compare remaining quantity
            marketCompare = -Long.compare(a.getQuantityRemaining(), b.getQuantityRemaining());
        } else if (a.type.isMarket()) {
            marketCompare = 1; // Only A is market
        } else if (b.type.isMarket()) {
            marketCompare = -1; // Only B is market
        } else {
            marketCompare = 0; // Both orders are limit. Incomparable yet.
        }
        return marketCompare;
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
        double volumeBefore = priceFulfilled;

        quantityFulfilled += quantity;

        long quantityAfter = quantityFulfilled;
        double volumeAfter = volumeBefore + quantity * price;

        priceFulfilled = quantityAfter != 0 ? volumeAfter / quantityAfter : 0;
    }
}
