package com.themrsung.mirae.market;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.skill.SkillType;
import org.jetbrains.annotations.NotNull;

/**
 * Market utilities.
 */
public final class Markets {
    /**
     * Returns the minimum tick size at the given price.
     *
     * @param price The price
     * @return The minimum tick size
     */
    public static double getTickSizeAt(double price) {
        double abs = Math.abs(price);

        if (abs < 100) {
            return 1;
        } else if (abs < 1000) {
            return 10;
        } else if (abs < 10000) {
            return 25;
        } else if (abs < 50000) {
            return 50;
        } else if (abs < 200000) {
            return 100;
        } else if (abs < 500000) {
            return 200;
        } else if (abs < 1000000) {
            return 500;
        } else {
            return 1000;
        }
    }

    /**
     * Snaps the price to the nearest tick size.
     *
     * @param price The price
     * @return The snapped price
     */
    public static double snapToNearestTick(double price) {
        double tick = getTickSizeAt(price);
        return Math.round(price / tick) * tick;
    }

    private static final double ACTIVE_FEE_RATE = 0.005;
    private static final double FIXED_FEE_RATE = 0.01;

    /**
     * Returns the base active fee rate.
     *
     * @return The base active fee rate.
     */
    public static double getActiveFeeRate() {
        return ACTIVE_FEE_RATE;
    }

    /**
     * Returns the active fee rate for the given account.
     *
     * @param account The account
     * @return The fee rate
     */
    public static double getActiveFeeRateFor(@NotNull Account account) {
        long level = account.getSkillLevel(SkillType.TRADING);
        double multiplier = Math.pow(0.99, level);

        return ACTIVE_FEE_RATE * multiplier;
    }

    /**
     * Returns the base fixed fee rate.
     *
     * @return The base rate
     */
    public static double getFixedFeeRate() {
        return FIXED_FEE_RATE;
    }

    /**
     * Returns the fixed fee rate for the given account.
     *
     * @param account The account
     * @return The fee rate
     */
    public static double getFixedFeeRateFor(@NotNull Account account) {
        long level = account.getSkillLevel(SkillType.TRADING);
        double multiplier = Math.pow(0.99, level);

        return FIXED_FEE_RATE * multiplier;
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Markets() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
