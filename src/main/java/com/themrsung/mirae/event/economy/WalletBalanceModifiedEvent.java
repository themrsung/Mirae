package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.economy.Wallet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Called when wallet balance is modified.
 */
public class WalletBalanceModifiedEvent extends WalletEvent {
    /**
     * Returns a new builder instance.
     *
     * @return The builder instance
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * The builder class.
     */
    public static class Builder {
        private Builder() {
        }

        EconomyCause cause;
        String message;
        Wallet wallet;
        double change;
        double balanceBefore;
        double balanceAfter;

        /**
         * Sets the cause.
         *
         * @param cause The cause
         * @return {@code this}
         */
        public @NotNull Builder cause(@Nullable EconomyCause cause) {
            this.cause = cause;
            return this;
        }

        /**
         * Sets the message.
         *
         * @param message The message
         * @return {@code this}
         */
        public @NotNull Builder message(@Nullable String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the wallet.
         *
         * @param wallet The wallet
         * @return {@code this}
         */
        public @NotNull Builder wallet(@Nullable Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        /**
         * Sets the change.
         *
         * @param change The net change
         * @return {@code this}
         */
        public @NotNull Builder change(double change) {
            this.change = change;
            return this;
        }

        /**
         * Sets the balance before.
         *
         * @param balanceBefore The balance before
         * @return {@code this}
         */
        public @NotNull Builder balanceBefore(double balanceBefore) {
            this.balanceBefore = balanceBefore;
            return this;
        }

        /**
         * Sets the balance after.
         *
         * @param balanceAfter The balance after
         * @return {@code this}
         */
        public @NotNull Builder balanceAfter(double balanceAfter) {
            this.balanceAfter = balanceAfter;
            return this;
        }

        /**
         * Builds the event.
         *
         * @return The event
         * @throws IllegalArgumentException When the parameters are invalid
         */
        public @NotNull WalletBalanceModifiedEvent build() throws IllegalArgumentException {
            Objects.requireNonNull(wallet);
            return new WalletBalanceModifiedEvent(wallet, cause, message, change, balanceBefore, balanceAfter);
        }
    }

    /**
     * Full constructor.
     *
     * @param wallet        The wallet
     * @param cause         The cause
     * @param message       The message
     * @param change        The net change
     * @param balanceBefore The balance before
     * @param balanceAfter  The balance after
     */
    protected WalletBalanceModifiedEvent(
            @NotNull Wallet wallet,
            @Nullable EconomyCause cause,
            @Nullable String message,
            double change,
            double balanceBefore,
            double balanceAfter
    ) {
        super(wallet, cause, message);
        this.change = change;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    protected final double change;
    protected final double balanceBefore;
    protected final double balanceAfter;

    /**
     * Returns the net change.
     *
     * @return The net change
     */
    public double getChange() {
        return change;
    }

    /**
     * Returns the absolute change.
     *
     * @return The absolute change
     */
    public double getAbsoluteChange() {
        return Math.abs(change);
    }

    /**
     * Returns whether the change is zero.
     *
     * @return {@code true} if {@code change == 0}
     */
    public boolean isZero() {
        return change == 0;
    }

    /**
     * Returns whether the change is positive.
     *
     * @return {@code true} if {@code change > 0}
     */
    public boolean isPositive() {
        return change > 0;
    }

    /**
     * Returns whether the change is negative.
     *
     * @return {@code true} if {@code change < 0}
     */
    public boolean isNegative() {
        return change < 0;
    }

    /**
     * Returns the balance before the change.
     *
     * @return The balance before
     */
    public double getBalanceBefore() {
        return balanceBefore;
    }

    /**
     * Returns the balance after the change.
     *
     * @return The balance after the change
     */
    public double getBalanceAfter() {
        return balanceAfter;
    }
}
