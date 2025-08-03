package com.themrsung.mirae.market.active;

/**
 * Order type.
 */
public enum OrderType {
    /**
     * Limit buy order. Sent by server.
     */
    BUY_LIMIT,

    /**
     * Market buy order. Sent buy players.
     */
    BUY_MARKET,

    /**
     * Limit sell order. Sent by server.
     */
    SELL_LIMIT,

    /**
     * Market sell order. Sent by players
     */
    SELL_MARKET;

    /**
     * Returns whether this is a buy order.
     *
     * @return {@code true} if buy, {@code false} if sell
     */
    public boolean isBuy() {
        return switch (this) {
            case BUY_LIMIT, BUY_MARKET -> true;
            default -> false;
        };
    }

    /**
     * Returns whether this is a market order.
     *
     * @return {@code true} if market, {@code false} if limit
     */
    public boolean isMarket() {
        return switch (this) {
            case BUY_MARKET, SELL_MARKET -> true;
            default -> false;
        };
    }

    /**
     * Returns whether this is a limit order.
     *
     * @return {@code true} if limit, {@code false} if market
     */
    public boolean isLimit() {
        return switch (this) {
            case BUY_LIMIT, SELL_LIMIT -> true;
            default -> false;
        };
    }
}
