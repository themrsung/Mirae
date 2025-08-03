package com.themrsung.mirae.market.active;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * The level of volatility.
 */
public enum VolatilityLevel {
    /**
     * Very stable. Equivalent to fixed-price markets in most cases.
     */
    VERY_STABLE(Component.text("매우 안정적").style(Style.style()
            .color(TextColor.fromHexString("#34ff3e"))
            .build()), 20, 25000),

    /**
     * Mostly stable. Has some fluctuations in price.
     */
    STABLE(Component.text("안정적").style(Style.style()
            .color(TextColor.fromHexString("#6fff81"))
            .build()), 10, 10000),

    /**
     * Moderate stability. Price fluctuates, but is reasonable.
     */
    MODERATE(Component.text("보통").style(Style.style()
            .color(TextColor.fromHexString("#407bff"))
            .build()), 10, 5000),

    /**
     * Illiquid and volatile. Good for speculative goods.
     */
    ILLIQUID(Component.text("불안정").style(Style.style()
            .color(TextColor.fromHexString("#ffdd47"))
            .build()), 5, 2500),

    /**
     * Rare. Very illiquid and incredibly volatile.
     */
    RARE(Component.text("매우 불안정").style(Style.style()
            .color(TextColor.fromHexString("#ff2a16"))
            .build()), 2, 500);

    /**
     * Creates a new volatility level.
     *
     * @param numSteps        The number of steps
     * @param quantityPerStep The quantity per step
     */
    VolatilityLevel(@NotNull Component displayName, int numSteps, long quantityPerStep) {
        this.displayName = displayName;
        this.numSteps = numSteps;
        this.quantityPerStep = quantityPerStep;
    }

    private final @NotNull Component displayName;
    private final int numSteps;
    private final long quantityPerStep;

    /**
     * Returns the display name of this volatility level.
     *
     * @return The display name
     */
    public @NotNull Component getDisplayName() {
        return displayName;
    }

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
}
