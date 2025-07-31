package com.themrsung.mirae.event.social;

import com.themrsung.mirae.event.MiraeEvent;
import com.themrsung.mirae.social.DirectMessage;
import org.jetbrains.annotations.NotNull;

/**
 * Called when DM is sent.
 */
public class DirectMessageSentEvent extends MiraeEvent {
    public DirectMessageSentEvent(@NotNull DirectMessage message) {
        this.message = message;
    }

    protected final @NotNull DirectMessage message;

    public @NotNull DirectMessage getMessage() {
        return message;
    }
}
