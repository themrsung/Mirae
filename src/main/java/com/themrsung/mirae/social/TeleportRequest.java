package com.themrsung.mirae.social;

import com.themrsung.mirae.account.Account;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Teleport request.
 *
 * @param uniqueId  The unique identifier
 * @param time      The time
 * @param sender    The sender
 * @param recipient The recipient
 * @param inbound   Whether this is an inbound ({@code /tpahere}) request
 */
public record TeleportRequest(
        @NotNull UUID uniqueId,
        @NotNull LocalDateTime time,
        @NotNull Account sender,
        @NotNull Account recipient,
        boolean inbound
) {
    /**
     * Creates and returns a new request.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param inbound   Whether it is inbound
     * @return The request
     */
    public static @NotNull TeleportRequest createRequest(@NotNull Account sender, @NotNull Account recipient, boolean inbound) {
        return new TeleportRequest(UUID.randomUUID(), LocalDateTime.now(), sender, recipient, inbound);
    }
}
