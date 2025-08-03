package com.themrsung.mirae.listener;

import com.themrsung.mirae.listener.command.AntiSpamListener;
import com.themrsung.mirae.listener.command.CommandTypoListener;
import com.themrsung.mirae.listener.economy.EconomyEventListener;
import com.themrsung.mirae.listener.gui.GUIActionListener;
import com.themrsung.mirae.listener.gui.QuickMenuListener;
import com.themrsung.mirae.listener.player.PlayerListener;
import com.themrsung.mirae.listener.player.ShulkerBoxListener;
import com.themrsung.mirae.listener.skill.SkillUpgradeListener;
import com.themrsung.mirae.listener.social.SocialSpyListener;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Listeners.
 */
public final class Listeners {
    public static final @NotNull PlayerListener PLAYER_LISTENER = new PlayerListener();
    public static final @NotNull CommandTypoListener COMMAND_TYPO_LISTENER = new CommandTypoListener();
    public static final @NotNull GUIActionListener GUI_ACTION_LISTENER = new GUIActionListener();
    public static final @NotNull EconomyEventListener ECONOMY_EVENT_LISTENER = new EconomyEventListener();
    public static final @NotNull ShulkerBoxListener SHULKER_BOX_LISTENER = new ShulkerBoxListener();
    public static final @NotNull SocialSpyListener SOCIAL_SPY_LISTENER = new SocialSpyListener();
    public static final @NotNull AntiSpamListener ANTI_SPAM_LISTENER = new AntiSpamListener();
    public static final @NotNull SkillUpgradeListener SKILL_UPGRADE_LISTENER = new SkillUpgradeListener();
    public static final @NotNull QuickMenuListener QUICK_MENU_LISTENER = new QuickMenuListener();

    /**
     * The set of all listeners.
     */
    private static final @NotNull Set<Listener> LISTENERS = Set.of(
            PLAYER_LISTENER,
            COMMAND_TYPO_LISTENER,
            GUI_ACTION_LISTENER,
            ECONOMY_EVENT_LISTENER,
            SHULKER_BOX_LISTENER,
            SOCIAL_SPY_LISTENER,
            ANTI_SPAM_LISTENER,
            SKILL_UPGRADE_LISTENER,
            QUICK_MENU_LISTENER
    );

    /**
     * Returns the set of all listeners.
     *
     * @return The set of all listeners
     */
    public static @NotNull Set<Listener> getListeners() {
        return LISTENERS;
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
