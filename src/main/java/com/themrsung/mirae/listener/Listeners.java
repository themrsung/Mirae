package com.themrsung.mirae.listener;

import com.themrsung.mirae.listener.abuse.AntiSpamListener;
import com.themrsung.mirae.listener.abuse.TransferAmountListener;
import com.themrsung.mirae.listener.banknote.BanknoteListener;
import com.themrsung.mirae.listener.command.CommandTypoListener;
import com.themrsung.mirae.listener.economy.EconomyEventListener;
import com.themrsung.mirae.listener.enchant.SuperShovelListener;
import com.themrsung.mirae.listener.enchant.ThreeByThreeMiningListener;
import com.themrsung.mirae.listener.gui.GUIActionListener;
import com.themrsung.mirae.listener.gui.QuickMenuListener;
import com.themrsung.mirae.listener.item.FoodListener;
import com.themrsung.mirae.listener.item.ItemListener;
import com.themrsung.mirae.listener.lootbox.LootBoxRedemptionListener;
import com.themrsung.mirae.listener.mining.CustomBlockSilkTouchListener;
import com.themrsung.mirae.listener.player.PlayerListener;
import com.themrsung.mirae.listener.player.ShulkerBoxListener;
import com.themrsung.mirae.listener.skill.SkillRestrictionListener;
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
    public static final @NotNull SkillRestrictionListener SKILL_RESTRICTION_LISTENER = new SkillRestrictionListener();
    public static final @NotNull QuickMenuListener QUICK_MENU_LISTENER = new QuickMenuListener();
    public static final @NotNull CustomBlockSilkTouchListener CUSTOM_ORE_SILK_TOUCH_MINING_LISTENER = new CustomBlockSilkTouchListener();
    public static final @NotNull TransferAmountListener TRANSFER_AMOUNT_LISTENER = new TransferAmountListener();
    public static final @NotNull LootBoxRedemptionListener LOOT_BOX_REDEMPTION_LISTENER = new LootBoxRedemptionListener();
    public static final @NotNull BanknoteListener BANKNOTE_LISTENER = new BanknoteListener();
    public static final @NotNull ThreeByThreeMiningListener THREE_BY_THREE_MINING_LISTENER = new ThreeByThreeMiningListener();
    public static final @NotNull SuperShovelListener SUPER_SHOVEL_LISTENER = new SuperShovelListener();
    public static final @NotNull FoodListener FOOD_LISTENER = new FoodListener();
    public static final @NotNull ItemListener ITEM_LISTENER = new ItemListener();

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
            SKILL_RESTRICTION_LISTENER,
            QUICK_MENU_LISTENER,
            CUSTOM_ORE_SILK_TOUCH_MINING_LISTENER,
            TRANSFER_AMOUNT_LISTENER,
            LOOT_BOX_REDEMPTION_LISTENER,
            BANKNOTE_LISTENER,
            THREE_BY_THREE_MINING_LISTENER,
            SUPER_SHOVEL_LISTENER,
            FOOD_LISTENER,
            ITEM_LISTENER
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
