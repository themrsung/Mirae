package com.themrsung.mirae.listener;

import com.themrsung.mirae.listener.command.CommandTypoListener;
import com.themrsung.mirae.listener.economy.EconomyEventListener;
import com.themrsung.mirae.listener.gui.GUIActionListener;
import com.themrsung.mirae.listener.player.PlayerListener;
import com.themrsung.mirae.listener.player.ShulkerBoxListener;
import com.themrsung.mirae.listener.social.SocialSpyListener;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Listeners.
 */
public final class Listeners {
    private static final @NotNull PlayerListener PLAYER_LISTENER = new PlayerListener();
    private static final @NotNull CommandTypoListener COMMAND_TYPO_LISTENER = new CommandTypoListener();
    private static final @NotNull GUIActionListener GUI_ACTION_LISTENER = new GUIActionListener();
    private static final @NotNull EconomyEventListener ECONOMY_EVENT_LISTENER = new EconomyEventListener();
    private static final @NotNull ShulkerBoxListener SHULKER_BOX_LISTENER = new ShulkerBoxListener();
    private static final @NotNull SocialSpyListener SOCIAL_SPY_LISTENER = new SocialSpyListener();

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

    public static @NotNull GUIActionListener getGuiActionListener() {
        return GUI_ACTION_LISTENER;
    }

    public static @NotNull EconomyEventListener getEconomyEventListener() {
        return ECONOMY_EVENT_LISTENER;
    }

    public static @NotNull ShulkerBoxListener getShulkerBoxListener() {
        return SHULKER_BOX_LISTENER;
    }

    public static @NotNull SocialSpyListener getSocialSpyListener() {
        return SOCIAL_SPY_LISTENER;
    }

    /**
     * Returns the set of all listeners.
     *
     * @return The set of all listeners
     */
    public static @NotNull Set<Listener> getListeners() {
        return Set.of(
                PLAYER_LISTENER,
                COMMAND_TYPO_LISTENER,
                GUI_ACTION_LISTENER,
                ECONOMY_EVENT_LISTENER,
                SHULKER_BOX_LISTENER,
                SOCIAL_SPY_LISTENER
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
