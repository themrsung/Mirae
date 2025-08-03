package com.themrsung.mirae.market;

import org.jetbrains.annotations.NotNull;

/**
 * Result of a price query.
 *
 * @param market   The market which was queried
 * @param quantity The quantity available
 * @param price    The unit price
 */
public record PriceQueryResult(
        @NotNull Market market,
        long quantity,
        double price
) {
    /**
     * Returns the volume of the result.
     *
     * @return The volume
     */
    public double volume() {
        return price * quantity;
    }
}
