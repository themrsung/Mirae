package com.themrsung.mirae.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The cause of an economy event.
 */
public interface EconomyCause {
    ///
    /// Default causes
    ///

    /**
     * Native deposit.
     */
    @NotNull EconomyCause NATIVE_DEPOSIT = new InitialCause("Deposit (Native)");

    /**
     * Native withdrawal.
     */
    @NotNull EconomyCause NATIVE_WITHDRAWAL = new InitialCause("Withdrawal (Native)");

    /**
     * Native transfer.
     */
    @NotNull EconomyCause NATIVE_TRANSFER = new InitialCause("Transfer (Native)");

    /**
     * Vault deposit.
     */
    @NotNull EconomyCause VAULT_DEPOSIT = new InitialCause("Deposit (Vault)");

    /**
     * Vault withdrawal.
     */
    @NotNull EconomyCause VAULT_WITHDRAWAL = new InitialCause("Withdrawal (Vault)");

    /**
     * Admin command.
     */
    @NotNull EconomyCause ADMIN_COMMAND = new InitialCause("Admin Command");

    /**
     * Item redemption.
     */
    @NotNull EconomyCause ITEM_REDEEMED = new InitialCause("Item Redeemed");

    /**
     * Withdrawn as item.
     */
    @NotNull EconomyCause WITHDRAWN_AS_ITEM = new InitialCause("Withdrawn as Item");

    /**
     * Initialized when loaded from disk.
     */
    @NotNull EconomyCause INITIALIZED = new InitialCause("Initialized");

    /**
     * Interest received to account.
     */
    @NotNull EconomyCause INTEREST_RECEIVED = new InitialCause("Interest Received");

    /**
     * Market transaction. (buy)
     */
    @NotNull EconomyCause MARKET_TRANSACTION_BUY = new InitialCause("Market Transaction (BUY)");

    /**
     * Market transaction. (sell)
     */
    @NotNull EconomyCause MARKET_TRANSACTION_SELL = new InitialCause("Market Transaction (SELL)");

    /**
     * Buy from donor shop.
     */
    @NotNull EconomyCause DONOR_SHOP_BUY = new InitialCause("Bought item from Donor Shop");

    ///
    /// Custom cause
    ///

    /**
     * Creates a new initial economy cause.
     *
     * @param message The content
     * @return The cause
     */
    static @NotNull EconomyCause createInitialCause(@Nullable String message) {
        return new InitialCause(message);
    }

    ///
    /// Properties
    ///

    /**
     * Returns the cause.
     *
     * @return The cause if present, {@code null} otherwise
     */
    @Nullable EconomyCause getCause();

    /**
     * Returns the content.
     *
     * @return The content if present, {@code null} otherwise
     */
    @Nullable String getMessage();

    /**
     * Initial cause.
     */
    final class InitialCause implements EconomyCause {
        private InitialCause(@Nullable String message) {
            this.message = message;
        }

        private final @Nullable String message;

        @Override
        public @Nullable EconomyCause getCause() {
            return null;
        }

        @Override
        public @Nullable String getMessage() {
            return message;
        }
    }
}
