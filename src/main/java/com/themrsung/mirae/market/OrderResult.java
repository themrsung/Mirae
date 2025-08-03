package com.themrsung.mirae.market;

import com.themrsung.mirae.account.Account;
import org.jetbrains.annotations.NotNull;

/**
 * The result of an order.
 *
 * @param market            The market
 * @param account           The account
 * @param quantityOrdered   The quantity ordered
 * @param quantityFulfilled The quantity fulfilled
 * @param priceOrdered      The price ordered
 * @param priceFulfilled    The price fulfilled
 */
public record OrderResult(
        @NotNull Market market,
        @NotNull Account account,
        long quantityOrdered,
        long quantityFulfilled,
        double priceOrdered,
        double priceFulfilled
) {
    /**
     * Returns the volume ordered.
     *
     * @return The volume ordered
     */
    public double volumeOrdered() {
        return priceOrdered * quantityOrdered;
    }

    /**
     * Returns the volume fulfilled.
     *
     * @return The volume fulfilled
     */
    public double volumeFulfilled() {
        return priceFulfilled * quantityFulfilled;
    }

    /**
     * Returns whether the order was fully fulfilled.
     *
     * @return {@code true} if fully fulfilled
     */
    public boolean allFulfilled() {
        return quantityFulfilled >= quantityOrdered;
    }

    /**
     * Returns whether the order was not fulfilled at all.
     *
     * @return {@code true} if no quantity was fulfilled
     */
    public boolean noneFulfilled() {
        return quantityFulfilled <= 0;
    }
}
