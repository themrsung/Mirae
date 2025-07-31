package com.themrsung.mirae.event.economy;

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
