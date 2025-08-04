package com.themrsung.mirae.market.active;

/**
 * The level of volatility.
 */
public enum VolatilityLevel {
    /**
     * Very stable. Equivalent to fixed-price markets in most cases.
     */
    VERY_STABLE(20, 25000),

    /**
     * Mostly stable. Has some fluctuations in price.
     */
    STABLE(10, 10000),

    /**
     * Moderate stability. Price fluctuates, but is reasonable.
     */
    MODERATE(10, 5000),

    /**
     * Illiquid and volatile. Good for speculative goods.
     */
    ILLIQUID(5, 2500),

    /**
     * Rare. Very illiquid and incredibly volatile.
     */
    RARE(2, 500),

    ;

    /**
     * Creates a new volatility level.
     *
     * @param numSteps        The number of steps
     * @param quantityPerStep The quantity per step
     */
    VolatilityLevel(int numSteps, long quantityPerStep) {
        this.numSteps = numSteps;
        this.quantityPerStep = quantityPerStep;
    }

    private final int numSteps;
    private final long quantityPerStep;

    /**
     * Returns the number of steps this volatility levels gives liquidity to.
     *
     * @return The number of steps
     */
    public int getNumSteps() {
        return numSteps;
    }

    /**
     * Returns the quantity of each step.
     *
     * @return The quantity of each step
     */
    public long getQuantityPerStep() {
        return quantityPerStep;
    }

    /**
     * Returns the total order volume.
     *
     * @return The total order volume
     */
    public long getTotalOrderCount() {
        return numSteps * quantityPerStep * 2;
    }
}
