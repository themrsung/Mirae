package com.themrsung.mirae.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A Mirae event.
 */
public abstract class MiraeEvent extends Event {
    /**
     * Creates a new event.
     */
    public MiraeEvent() {
        super();
    }

    /**
     * Creates a new event.
     *
     * @param async Whether the event is asynchronous
     */
    public MiraeEvent(boolean async) {
        super(async);
    }

    /// Boilerplate

    private static final @NotNull HandlerList handlers = new HandlerList();

    /**
     * Boilerplate getter method.
     *
     * @return The handler list
     */
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
