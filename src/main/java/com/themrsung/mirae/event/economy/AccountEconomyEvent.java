package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Superclass for account-related economy events.
 */
public abstract class AccountEconomyEvent extends EconomyEvent {
    /**
     * Creates a new event.
     *
     * @param account The associated account
     */
    public AccountEconomyEvent(@NotNull Account account) {
        super();

        this.account = account;
    }

    /**
     * Creates a new event.
     *
     * @param account The associated account
     * @param cause   The cause
     */
    public AccountEconomyEvent(@NotNull Account account, @Nullable EconomyCause cause) {
        super(cause);

        this.account = account;
    }

    /**
     * Creates a new event.
     *
     * @param account The associated account
     * @param cause   The cause
     * @param message The message
     */
    public AccountEconomyEvent(@NotNull Account account, @Nullable EconomyCause cause, @Nullable String message) {
        super(cause, message);

        this.account = account;
    }

    /**
     * Creates a new event.
     *
     * @param uniqueId The unique identifier
     * @param time     The time
     * @param account  The associated account
     * @param cause    The cause
     * @param message  The message
     */
    public AccountEconomyEvent(@NotNull UUID uniqueId, @NotNull LocalDateTime time, @NotNull Account account, @Nullable EconomyCause cause, @Nullable String message) {
        super(uniqueId, time, cause, message);

        this.account = account;
    }

    protected final @NotNull Account account;

    /**
     * Returns the account.
     *
     * @return The account
     */
    public @NotNull Account getAccount() {
        return account;
    }
}
