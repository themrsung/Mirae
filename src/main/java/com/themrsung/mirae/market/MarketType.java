package com.themrsung.mirae.market;

import com.themrsung.mirae.market.active.ActivePriceMarket;
import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import org.jetbrains.annotations.NotNull;

/**
 * Market type.
 */
public enum MarketType {
    /**
     * Am active price market.
     *
     * @see ActivePriceMarket
     */
    ACTIVE_PRICE(ActivePriceMarket.class),

    /**
     * A fixed type market.
     *
     * @see FixedPriceMarket
     */
    FIXED_PRICE(FixedPriceMarket.class),
    ;

    /**
     * Creates a new market type.
     *
     * @param implementation The class type
     */
    MarketType(@NotNull Class<? extends Market> implementation) {
        this.implementation = implementation;
    }

    private final @NotNull Class<? extends Market> implementation;

    /**
     * Returns the class type.
     *
     * @return The class type
     */
    public @NotNull Class<? extends Market> getImplementation() {
        return implementation;
    }
}
