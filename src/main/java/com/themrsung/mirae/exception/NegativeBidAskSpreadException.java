package com.themrsung.mirae.exception;

import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the Bid-Ask spread is negative in a {@link FixedPriceMarket}.
 */
public class NegativeBidAskSpreadException extends RuntimeException {
    /**
     * Creates a new exception.
     *
     * @param market    The market which caused this exception
     * @param buyPrice  The buy price which caused this exception
     * @param sellPrice The sell price which caused this exception
     */
    public NegativeBidAskSpreadException(@NotNull FixedPriceMarket market, double buyPrice, double sellPrice) {
        this.market = market;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    private final @NotNull FixedPriceMarket market;
    private final double buyPrice;
    private final double sellPrice;

    public @NotNull FixedPriceMarket getMarket() {
        return market;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
