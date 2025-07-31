package com.themrsung.mirae.economy;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An economy result.
 */
public final class EconomyResult {
    ///
    /// Results
    ///

    /**
     * Success via native API.
     */
    public static final @NotNull EconomyResult SUCCESS_NATIVE = new EconomyResult(true, "Success");

    /**
     * Success via Vault API.
     */
    public static final @NotNull EconomyResult SUCCESS_VAULT = new EconomyResult(true, "Success via Vault");

    /**
     * Economy frozen.
     */
    public static final @NotNull EconomyResult FAILURE_ECONOMY_FROZEN = new EconomyResult(false, "Economy Frozen");

    /**
     * Own account frozen.
     */
    public static final @NotNull EconomyResult FAILURE_ACCOUNT_FROZEN = new EconomyResult(false, "Account Frozen");

    /**
     * Counterparty's account frozen.
     */
    public static final @NotNull EconomyResult FAILURE_COUNTERPARTY_ACCOUNT_FROZEN = new EconomyResult(false, "Counterparty Account Frozen");

    /**
     * Failure due to invalid input.
     */
    public static final @NotNull EconomyResult FAILURE_INVALID_INPUT = new EconomyResult(false, "Invalid Input");

    /**
     * Failure due to an invalid account.
     */
    public static final @NotNull EconomyResult FAILURE_INVALID_ACCOUNT = new EconomyResult(false, "Invalid Account");

    /**
     * Failure due to insufficient funds.
     */
    public static final @NotNull EconomyResult FAILURE_INSUFFICIENT_FUNDS = new EconomyResult(false, "Insufficient Funds");

    /**
     * Creates and returns a new result.
     *
     * @param success Whether it was a success
     * @param message The content
     * @return The result
     */
    public static @NotNull EconomyResult createResult(boolean success, @NotNull String message) {
        return new EconomyResult(success, message);
    }

    ///
    /// Body
    ///

    /**
     * Creates a new result.
     *
     * @param success Whether it was successful
     * @param message The content
     */
    private EconomyResult(boolean success, @NotNull String message) {
        this.success = success;
        this.message = message;
    }

    private final boolean success;
    private final @NotNull String message;

    /**
     * Returns whether this result is a success.
     *
     * @return {@code true} if success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the content.
     *
     * @return The content
     */
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EconomyResult r)) return false;
        return success == r.success && Objects.equals(message, r.message);
    }
}
