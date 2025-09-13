package com.themrsung.mirae.market.active;

/**
 * The level of volatility.
 */
public enum VolatilityLevel {
    /**
     * Very stable. Equivalent to fixed-price markets in most cases.
     */
    VERY_STABLE(20, 4),

    /**
     * Mostly stable. Has some fluctuations in price.
     */
    STABLE(10, 3),

    /**
     * Moderate stability. Price fluctuates, but is reasonable.
     */
    MODERATE(10, 2),

    /**
     * Illiquid and volatile. Good for speculative goods.
     */
    ILLIQUID(5, 1),

    /**
     * Rare. Very illiquid and incredibly volatile.
     */
    RARE(2, 0.5),

    /**
     * Speculative. Do not use frequently.
     */
    SPECULATIVE(20, 0.25);

    /**
     * Creates a new volatility level.
     *
     * @param numSteps        The number of steps
     * @param stacksPerStep The stacks per step
     */
    VolatilityLevel(int numSteps, double stacksPerStep) {
        this.numSteps = numSteps;
        this.stacksPerStep = stacksPerStep;
        this.quantityPerStep = (long) stacksPerStep * 64;
    }

    private final int numSteps;
    private final double stacksPerStep;
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
    @Deprecated
    public long getQuantityPerStep() {
        return quantityPerStep;
    }

    /**
     * Returns the nmumber of stacks per step.
     * @return The number of stacks per step
     */
    public double getStacksPerStep() {
        return stacksPerStep;
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
