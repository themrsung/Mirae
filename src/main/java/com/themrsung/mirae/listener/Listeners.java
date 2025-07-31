package com.themrsung.mirae.listener;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Listeners.
 */
public final class Listeners {
    private static final @NotNull PlayerListener PLAYER_LISTENER = new PlayerListener();
    private static final @NotNull CommandTypoListener COMMAND_TYPO_LISTENER = new CommandTypoListener();

    /**
     * Returns the player listener.
     *
     * @return The player listener
     */
    public static @NotNull PlayerListener getPlayerListener() {
        return PLAYER_LISTENER;
    }

    public static @NotNull CommandTypoListener getCommandTypoListener() {
        return COMMAND_TYPO_LISTENER;
    }

    /**
     * Returns the set of all listeners.
     *
     * @return The set of all listeners
     */
    public static @NotNull Set<Listener> getListeners() {
        return Set.of(
                PLAYER_LISTENER,
                COMMAND_TYPO_LISTENER
        );
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Listeners() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
