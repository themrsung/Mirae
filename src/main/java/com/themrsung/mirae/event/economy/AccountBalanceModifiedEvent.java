package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Called when balance is modified.
 */
public class AccountBalanceModifiedEvent extends AccountEconomyEvent {
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
        protected double balanceChange;
        protected double balanceBefore;
        protected double balanceAfter;

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
        public @NotNull Builder balanceChange(double change) {
            this.balanceChange = change;
            return this;
        }

        /**
         * Sets the balance before.
         *
         * @param before The balance before
         * @return The builder instance
         */
        public @NotNull Builder balanceBefore(double before) {
            this.balanceBefore = before;
            return this;
        }

        /**
         * Sets the balance after.
         *
         * @param after The balance after
         * @return The builder instance
         */
        public @NotNull Builder balanceAfter(double after) {
            this.balanceAfter = after;
            return this;
        }

        /**
         * Builds and returns the instance.
         *
         * @return The instance
         * @throws IllegalArgumentException When a required parameter is {@code null}
         */
        public @NotNull AccountBalanceModifiedEvent build() throws IllegalArgumentException {
            return new AccountBalanceModifiedEvent(
                    uniqueId,
                    time,
                    account,
                    cause,
                    message,
                    balanceChange,
                    balanceBefore,
                    balanceAfter
            );
        }
    }

    protected AccountBalanceModifiedEvent(
            @NotNull UUID uniqueId,
            @NotNull LocalDateTime time,
            @NotNull Account account,
            @Nullable EconomyCause cause,
            @Nullable String message,
            double balanceChange,
            double balanceBefore,
            double balanceAfter
    ) {
        super(uniqueId, time, account, cause, message);

        this.balanceChange = balanceChange;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    protected final double balanceBefore;
    protected final double balanceChange;
    protected final double balanceAfter;

    /**
     * Returns the balance before this event.
     *
     * @return The balance before this event
     */
    public double getBalanceBefore() {
        return balanceBefore;
    }

    /**
     * Returns the balance change of this event.
     *
     * @return The balance change of this event
     */
    public double getBalanceChange() {
        return balanceChange;
    }

    /**
     * Returns the balance after this event.
     *
     * @return The balance after this event
     */
    public double getBalanceAfter() {
        return balanceAfter;
    }

    /**
     * Returns the absolute change.
     *
     * @return The absolute change
     */
    public double getAbsoluteChange() {
        return Math.abs(balanceChange);
    }

    /**
     * Returns whether the balance has changed.
     *
     * @return {@code true} if changed
     */
    public boolean hasBalanceChanged() {
        return balanceChange != 0;
    }

    /**
     * Returns whether the balance has increased.
     *
     * @return {@code true} if increased
     */
    public boolean hasBalanceIncreased() {
        return balanceChange > 0;
    }

    /**
     * Returns whether the balance has decreased.
     *
     * @return {@code true} if decreased
     */
    public boolean hasBalanceDecreased() {
        return balanceChange < 0;
    }
}
