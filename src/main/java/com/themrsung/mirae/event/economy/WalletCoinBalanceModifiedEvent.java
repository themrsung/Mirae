package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.economy.Wallet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Called when wallet balance is modified.
 */
public class WalletCoinBalanceModifiedEvent extends WalletEvent {
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
        long change;
        long coinBalanceBefore;
        long coinBalanceAfter;

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
        public @NotNull Builder change(long change) {
            this.change = change;
            return this;
        }

        /**
         * Sets the balance before.
         *
         * @param coinBalanceBefore The balance before
         * @return {@code this}
         */
        public @NotNull Builder coinBalanceBefore(long coinBalanceBefore) {
            this.coinBalanceBefore = coinBalanceBefore;
            return this;
        }

        /**
         * Sets the balance after.
         *
         * @param coinBalanceAfter The balance after
         * @return {@code this}
         */
        public @NotNull Builder coinBalanceAfter(long coinBalanceAfter) {
            this.coinBalanceAfter = coinBalanceAfter;
            return this;
        }

        /**
         * Builds the event.
         *
         * @return The event
         * @throws IllegalArgumentException When the parameters are invalid
         */
        public @NotNull WalletCoinBalanceModifiedEvent build() throws IllegalArgumentException {
            Objects.requireNonNull(wallet);
            return new WalletCoinBalanceModifiedEvent(wallet, cause, message, change, coinBalanceBefore, coinBalanceAfter);
        }
    }

    /**
     * Full constructor.
     *
     * @param wallet            The wallet
     * @param cause             The cause
     * @param message           The message
     * @param change            The net change
     * @param coinBalanceBefore The balance before
     * @param coinBalanceAfter  The balance after
     */
    protected WalletCoinBalanceModifiedEvent(
            @NotNull Wallet wallet,
            @Nullable EconomyCause cause,
            @Nullable String message,
            long change,
            long coinBalanceBefore,
            long coinBalanceAfter
    ) {
        super(wallet, cause, message);
        this.change = change;
        this.coinBalanceBefore = coinBalanceBefore;
        this.coinBalanceAfter = coinBalanceAfter;
    }

    protected final long change;
    protected final long coinBalanceBefore;
    protected final long coinBalanceAfter;

    /**
     * Returns the net change.
     *
     * @return The net change
     */
    public long getChange() {
        return change;
    }

    /**
     * Returns the absolute change.
     *
     * @return The absolute change
     */
    public long getAbsoluteChange() {
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
    public long getCoinBalanceBefore() {
        return coinBalanceBefore;
    }

    /**
     * Returns the balance after the change.
     *
     * @return The balance after the change
     */
    public long getCoinBalanceAfter() {
        return coinBalanceAfter;
    }
}
