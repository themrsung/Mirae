package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EquityToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Called when token balance is modified.
 */
public class AccountTokenBalanceModifiedEvent extends AccountEconomyEvent {
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
        protected EquityToken token;
        protected long tokenBalanceChange;
        protected long tokenBalanceBefore;
        protected long tokenBalanceAfter;

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
         * Sets the token.
         * @param token The token
         * @return The builder instance
         */
        public @NotNull Builder token(@NotNull EquityToken token) {
            this.token = token;
            return this;
        }

        /**
         * Sets the balance change.
         *
         * @param change The change
         * @return The builder instance
         */
        public @NotNull Builder tokenBalanceChange(long change) {
            this.tokenBalanceChange = change;
            return this;
        }

        /**
         * Sets the balance before.
         *
         * @param before The balance before
         * @return The builder instance
         */
        public @NotNull Builder tokenBalanceBefore(long before) {
            this.tokenBalanceBefore = before;
            return this;
        }

        /**
         * Sets the balance after.
         *
         * @param after The balance after
         * @return The builder instance
         */
        public @NotNull Builder tokenBalanceAfter(long after) {
            this.tokenBalanceAfter = after;
            return this;
        }

        /**
         * Builds and returns the instance.
         *
         * @return The instance
         * @throws IllegalArgumentException When a required parameter is {@code null}
         */
        public @NotNull AccountTokenBalanceModifiedEvent build() throws IllegalArgumentException {
            return new AccountTokenBalanceModifiedEvent(
                    uniqueId,
                    time,
                    account,
                    cause,
                    message,
                    token,
                    tokenBalanceChange,
                    tokenBalanceBefore,
                    tokenBalanceAfter
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
     * @param tokenBalanceChange The coin balance change
     * @param tokenBalanceBefore The coin balance before
     * @param tokenBalanceAfter  The coin balance after
     */
    protected AccountTokenBalanceModifiedEvent(
            @NotNull UUID uniqueId,
            @NotNull LocalDateTime time,
            @NotNull Account account,
            @Nullable EconomyCause cause,
            @Nullable String message,
            @NotNull EquityToken token,
            long tokenBalanceChange,
            long tokenBalanceBefore,
            long tokenBalanceAfter
    ) {
        super(uniqueId, time, account, cause, message);

        this.token = token;
        this.tokenBalanceChange = tokenBalanceChange;
        this.tokenBalanceBefore = tokenBalanceBefore;
        this.tokenBalanceAfter = tokenBalanceAfter;
    }

    protected final @NotNull EquityToken token;
    protected final long tokenBalanceBefore;
    protected final long tokenBalanceChange;
    protected final long tokenBalanceAfter;

    /**
     * Returns the token which was changed.
     * @return The token
     */
    public @NotNull EquityToken getToken() {
        return token;
    }

    /**
     * Returns the balance before this event.
     *
     * @return The balance before this event
     */
    public long getTokenBalanceBefore() {
        return tokenBalanceBefore;
    }

    /**
     * Returns the balance change of this event.
     *
     * @return The balance change of this event
     */
    public long getTokenBalanceChange() {
        return tokenBalanceChange;
    }

    /**
     * Returns the balance after this event.
     *
     * @return The balance after this event
     */
    public long getTokenBalanceAfter() {
        return tokenBalanceAfter;
    }

    /**
     * Returns the absolute change.
     *
     * @return The absolute change
     */
    public long getAbsoluteChange() {
        return Math.abs(tokenBalanceChange);
    }

    /**
     * Returns whether the coin balance has changed.
     *
     * @return {@code true} if changed
     */
    public boolean hasTokenBalanceChanged() {
        return tokenBalanceChange != 0;
    }

    /**
     * Returns whether the coin balance has increased.
     *
     * @return {@code true} if increased
     */
    public boolean hasTokenBalanceIncreased() {
        return tokenBalanceChange > 0;
    }

    /**
     * Returns whether the coin balance has decreased.
     *
     * @return {@code true} if decreased
     */
    public boolean hasTokenBalanceDecreased() {
        return tokenBalanceChange < 0;
    }
}
