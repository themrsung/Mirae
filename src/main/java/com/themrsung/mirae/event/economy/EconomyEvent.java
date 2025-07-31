package com.themrsung.mirae.event.economy;

import com.themrsung.mirae.event.MiraeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Superclass for economy events.
 */
public abstract class EconomyEvent extends MiraeEvent implements EconomyCause {
    /**
     * Creates a new economy event.
     */
    public EconomyEvent() {
        this(UUID.randomUUID(), LocalDateTime.now(), null, null);
    }

    /**
     * Creates a new event.
     *
     * @param cause The cause
     */
    public EconomyEvent(@Nullable EconomyCause cause) {
        this(UUID.randomUUID(), LocalDateTime.now(), cause, null);
    }

    /**
     * Creates a new event.
     *
     * @param cause   The cause
     * @param message The content
     */
    public EconomyEvent(@Nullable EconomyCause cause, @Nullable String message) {
        this(UUID.randomUUID(), LocalDateTime.now(), cause, message);
    }

    /**
     * Creates a new event.
     *
     * @param uniqueId The unique identifier
     * @param time     The time
     * @param cause    The cause
     * @param message  The content
     */
    public EconomyEvent(@NotNull UUID uniqueId, @NotNull LocalDateTime time, @Nullable EconomyCause cause, @Nullable String message) {
        super();

        this.uniqueId = uniqueId;
        this.time = time;
        this.cause = cause;
        this.message = message;
    }

    protected final @NotNull UUID uniqueId;
    protected final @NotNull LocalDateTime time;
    protected final @Nullable EconomyCause cause;
    protected final @Nullable String message;

    /**
     * Returns the unique identifier.
     *
     * @return The unique identifier
     */
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Returns the time.
     *
     * @return The time
     */
    public @NotNull LocalDateTime getTime() {
        return time;
    }

    @Override
    public @Nullable EconomyCause getCause() {
        return cause;
    }

    @Override
    public @Nullable String getMessage() {
        return message;
    }
}
