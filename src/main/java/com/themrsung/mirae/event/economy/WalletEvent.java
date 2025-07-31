package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.economy.Wallet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Superclass for wallet-related events.
 */
public abstract class WalletEvent extends EconomyEvent {
    /**
     * Creates a new event.
     *
     * @param wallet The wallet which was involved.
     */
    public WalletEvent(@NotNull Wallet wallet) {
        super();

        this.wallet = wallet;
    }

    /**
     * Creates a new event.
     *
     * @param wallet The wallet
     * @param cause  The cause
     */
    public WalletEvent(@NotNull Wallet wallet, @Nullable EconomyCause cause) {
        super(cause);

        this.wallet = wallet;
    }

    /**
     * Creates a new event.
     *
     * @param wallet  The wallet
     * @param cause   The cause
     * @param message The message
     */
    public WalletEvent(@NotNull Wallet wallet, @Nullable EconomyCause cause, @Nullable String message) {
        super(cause, message);

        this.wallet = wallet;
    }

    protected final @NotNull Wallet wallet;

    /**
     * Returns the wallet which was involved.
     *
     * @return The wallet which was involved
     */
    public @NotNull Wallet getWallet() {
        return wallet;
    }
}
