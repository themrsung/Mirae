package com.themrsung.mirae.event.social;

import com.themrsung.mirae.event.MiraeEvent;
import com.themrsung.mirae.social.DirectMessage;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a DM is sent.
 */
public class DirectMessageSentEvent extends MiraeEvent {
    /**
     * Creates a new event.
     * @param message The message
     */
    public DirectMessageSentEvent(@NotNull DirectMessage message) {
        this.message = message;
    }

    protected final @NotNull DirectMessage message;

    /**
     * Returns the direct message which was sent.
     * @return The DM which was sent
     */
    public @NotNull DirectMessage getMessage() {
        return message;
    }
}
