package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Called when coin balance is modified.
 */
public class AccountCoinBalanceModifiedEvent extends AccountEconomyEvent {
    /**
     * Returns a new builder instance.
     *
     * @return A new builder instance
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * The builder class.
     */
    public static class Builder {
        /**
         * Private constructor.
         */
        private Builder() {
            this.uniqueId = UUID.randomUUID();
            this.time = LocalDateTime.now();
        }

        protected UUID uniqueId;
        protected LocalDateTime time;
        protected Account account;
        protected EconomyCause cause;
        protected String message;
        protected long coinBalanceChange;
        protected long coinBalanceBefore;
        protected long coinBalanceAfter;

        /**
         * Sets the unique identifier.
         *
         * @param uniqueId The unique identifier
         * @return The builder instance
         */
        public @NotNull Builder uniqueId(@NotNull UUID uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        /**
         * Sets the time.
         *
         * @param time The time
         * @return The builder instance
         */
        public @NotNull Builder time(@NotNull LocalDateTime time) {
            this.time = time;
            return this;
        }

        /**
         * Sets the account.
         *
         * @param account The account
         * @return The builder instance
         */
        public @NotNull Builder account(@NotNull Account account) {
            this.account = account;
            return this;
        }

        /**
         * Sets the cause.
         *
         * @param cause The cause
         * @return The builder instance
         */
        public @NotNull Builder cause(@Nullable EconomyCause cause) {
            this.cause = cause;
            return this;
        }

        /**
         * Sets the message.
         *
         * @param message The message
         * @return The builder instance
         */
        public @NotNull Builder message(@Nullable String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the balance change.
         *
         * @param change The change
         * @return The builder instance
         */
        public @NotNull Builder coinBalanceChange(long change) {
            this.coinBalanceChange = change;
            return this;
        }

        /**
         * Sets the balance before.
         *
         * @param before The balance before
         * @return The builder instance
         */
        public @NotNull Builder coinBalanceBefore(long before) {
            this.coinBalanceBefore = before;
            return this;
        }

        /**
         * Sets the balance after.
         *
         * @param after The balance after
         * @return The builder instance
         */
        public @NotNull Builder coinBalanceAfter(long after) {
            this.coinBalanceAfter = after;
            return this;
        }

        /**
         * Builds and returns the instance.
         *
         * @return The instance
         * @throws IllegalArgumentException When a required parameter is {@code null}
         */
        public @NotNull AccountCoinBalanceModifiedEvent build() throws IllegalArgumentException {
            return new AccountCoinBalanceModifiedEvent(
                    uniqueId,
                    time,
                    account,
                    cause,
                    message,
                    coinBalanceChange,
                    coinBalanceBefore,
                    coinBalanceAfter
            );
        }
    }

    /**
     * Protected constructor.
     *
     * @param uniqueId          The unique identifier
     * @param time              The time
     * @param account           The account
     * @param cause             The cause
     * @param message           The message
     * @param coinBalanceChange The coin balance change
     * @param coinBalanceBefore The coin balance before
     * @param coinBalanceAfter  The coin balance after
     */
    protected AccountCoinBalanceModifiedEvent(
            @NotNull UUID uniqueId,
            @NotNull LocalDateTime time,
            @NotNull Account account,
            @Nullable EconomyCause cause,
            @Nullable String message,
            long coinBalanceChange,
            long coinBalanceBefore,
            long coinBalanceAfter
    ) {
        super(uniqueId, time, account, cause, message);

        this.coinBalanceChange = coinBalanceChange;
        this.coinBalanceBefore = coinBalanceBefore;
        this.coinBalanceAfter = coinBalanceAfter;
    }

    protected final long coinBalanceBefore;
    protected final long coinBalanceChange;
    protected final long coinBalanceAfter;

    /**
     * Returns the balance before this event.
     *
     * @return The balance before this event
     */
    public long getCoinBalanceBefore() {
        return coinBalanceBefore;
    }

    /**
     * Returns the balance change of this event.
     *
     * @return The balance change of this event
     */
    public long getCoinBalanceChange() {
        return coinBalanceChange;
    }

    /**
     * Returns the balance after this event.
     *
     * @return The balance after this event
     */
    public long getCoinBalanceAfter() {
        return coinBalanceAfter;
    }

    /**
     * Returns the absolute change.
     *
     * @return The absolute change
     */
    public long getAbsoluteChange() {
        return Math.abs(coinBalanceChange);
    }

    /**
     * Returns whether the coin balance has changed.
     *
     * @return {@code true} if changed
     */
    public boolean hasCoinBalanceChanged() {
        return coinBalanceChange != 0;
    }

    /**
     * Returns whether the coin balance has increased.
     *
     * @return {@code true} if increased
     */
    public boolean hasCoinBalanceIncreased() {
        return coinBalanceChange > 0;
    }

    /**
     * Returns whether the coin balance has decreased.
     *
     * @return {@code true} if decreased
     */
    public boolean hasCoinBalanceDecreased() {
        return coinBalanceChange < 0;
    }
}
