package com.themrsung.mirae.listener.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.event.economy.AccountBalanceModifiedEvent;
import com.themrsung.mirae.event.skill.AccountSkillLevelModifiedEvent;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Skill upgrade listener.
 */
public class SkillUpgradeListener implements Listener {
    private static final @NotNull Set<Long> TOAST_LEVELS = Set.of(
            10L,
            20L,
            30L,
            40L,
            50L,
            60L,
            70L,
            80L,
            90L,
            100L,
            125L,
            150L,
            175L,
            200L,
            250L,
            300L,
            400L,
            500L,
            600L,
            700L,
            800L,
            900L,
            1000L
    );

    @EventHandler
    public void onSkillLevelChanged(AccountSkillLevelModifiedEvent e) {
        Account account = e.getAccount();
        Player player = account.getPlayer();

        if (player == null || !player.isOnline() || e.getLevelChange() == 0) return;

        boolean up = e.getLevelChange() > 0;
        Component message = getSkillLevelChangeMessage(e, up);

        if (up) {
            if (TOAST_LEVELS.contains(e.getLevelAfter())) {
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                Bukkit.broadcast(Component.text("[").style(MX.STYLE_NORMAL)
                        .append(Component.text("!").style(MX.STYLE_GOOD))
                        .append(Component.text("]").style(MX.STYLE_NORMAL))
                        .appendSpace()
                        .append(message));
            } else {
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage(message);
            }
        } else {
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            player.sendMessage(message);
        }

        Bukkit.getConsoleSender().sendMessage(message);
    }

    private static @NotNull Component getSkillLevelChangeMessage(AccountSkillLevelModifiedEvent e, boolean up) {
        long absoluteChange = Math.abs(e.getLevelChange());

        return e.getSkillType().getDisplayName()
                .append(Component.text(" 스킬 ").style(MX.STYLE_NORMAL))
                .append(Component.text(e.getLevelAfter() + "레벨").style(MX.STYLE_SPECIAL))
                .append(Component.text("을 달성했습니다!").style(MX.STYLE_NORMAL))
                .appendSpace()
                .append(Component.text("(").style(MX.STYLE_NORMAL))
                .append(up ? Component.text(absoluteChange + "레벨 상승").style(MX.STYLE_BUY) : Component.text(absoluteChange + "레벨 하락").style(MX.STYLE_SELL))
                .append(Component.text(")").style(MX.STYLE_NORMAL));
    }

    @EventHandler
    public void onBalanceModified(AccountBalanceModifiedEvent e) {
        if (e.getAbsoluteChange() < 100000) return;

        Account account = e.getAccount();

        double numerator;
        EconomyCause cause = e.getCause();

        if (Objects.equals(cause, EconomyCause.MARKET_TRANSACTION_BUY) || Objects.equals(cause, EconomyCause.MARKET_TRANSACTION_SELL)) {
            numerator = 0.95;
        } else if (Objects.equals(cause, EconomyCause.NATIVE_TRANSFER)) {
            numerator = 1.05;
        } else {
            numerator = 0;
        }

        if (numerator == 0) {
            return;
        }

        double chance = numerator / 100;

        Random random = new Random();
        double random1 = random.nextDouble();

        if (random1 < chance) {
            account.incrementLevel(SkillType.TRADING);
        }
    }

    private static final @NotNull Map<Material, Double> ORE_CHANCE_MAP;

    static {
        Map<Material, Double> map = new HashMap<>();

        map.put(Material.STONE, 1d / 50000);
        map.put(Material.DEEPSLATE, 1d / 35000);
        map.put(Material.COAL_ORE, 1d / 3500);
        map.put(Material.DEEPSLATE_COAL_ORE, 1d / 3000);
        map.put(Material.IRON_ORE, 1d / 1500);
        map.put(Material.DEEPSLATE_IRON_ORE, 1d / 1000);
        map.put(Material.COPPER_ORE, 1d / 750);
        map.put(Material.DEEPSLATE_COPPER_ORE, 1d / 500);
        map.put(Material.GOLD_ORE, 1d / 750);
        map.put(Material.DEEPSLATE_GOLD_ORE, 1d / 500);
        map.put(Material.LAPIS_ORE, 1d / 500);
        map.put(Material.DEEPSLATE_LAPIS_ORE, 1d / 250);
        map.put(Material.REDSTONE_ORE, 1d / 500);
        map.put(Material.DEEPSLATE_REDSTONE_ORE, 1d / 250);
        map.put(Material.NETHER_QUARTZ_ORE, 1d / 250);
        map.put(Material.EMERALD_ORE, 1d / 200);
        map.put(Material.DEEPSLATE_EMERALD_ORE, 1d / 100);
        map.put(Material.DIAMOND_ORE, 1d / 100);
        map.put(Material.DEEPSLATE_DIAMOND_ORE, 1d / 75);

        ORE_CHANCE_MAP = Map.copyOf(map);
    }

    @EventHandler
    public void onOreMined(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemMeta meta = tool.getItemMeta();

        if (meta != null && meta.hasEnchant(Enchantment.SILK_TOUCH)) return;

        Block block = e.getBlock();

        if (!ORE_CHANCE_MAP.containsKey(block.getType())) {
            return;
        }

        double chance = ORE_CHANCE_MAP.getOrDefault(block.getType(), 0d);

        Random random = new Random();
        double random1 = random.nextDouble();

        if (random1 < chance) {
            account.incrementLevel(SkillType.MINING);
        }
    }

    @EventHandler
    public void onAncientDebrisCooked(FurnaceExtractEvent e) {
        if (e.getItemType() != Material.NETHERITE_SCRAP) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Random random = new Random();
        double random1 = random.nextDouble();

        if (random1 < (1d / 50)) {
            account.incrementLevel(SkillType.MINING);
        }
    }

    private static final @NotNull Map<Material, Double> WOOD_CHANCE_MAP = Map.of(
            Material.OAK_LOG, 1d / 500,
            Material.BIRCH_LOG, 1d / 500,
            Material.SPRUCE_LOG, 1d / 500,
            Material.JUNGLE_LOG, 1d / 500,
            Material.ACACIA_LOG, 1d / 500,
            Material.DARK_OAK_LOG, 1d / 500,
            Material.PALE_OAK_LOG, 1d / 500,
            Material.CHERRY_LOG, 1d / 500,
            Material.MANGROVE_LOG, 1d / 500
    );

    @EventHandler
    public void onWoodChopped(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Block block = e.getBlock();

        if (!WOOD_CHANCE_MAP.containsKey(block.getType())) {
            return;
        }

        double chance = WOOD_CHANCE_MAP.getOrDefault(block.getType(), 0d);

        Random random = new Random();
        double random1 = random.nextDouble();

        if (random1 < chance) {
            account.incrementLevel(SkillType.CARPENTRY);
        }
    }

    private static final @NotNull Map<Material, Double> CROP_CHANCE_MAP = Map.of(
            Material.WHEAT, 1d / 500,
            Material.POTATO, 1d / 500,
            Material.BEETROOT, 1d / 500,
            Material.CARROT, 1d / 500,
            Material.MELON, 1d / 500,
            Material.PUMPKIN, 1d / 500,
            Material.SWEET_BERRY_BUSH, 1d / 500
    );

    @EventHandler
    public void onCropCollected(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Block block = e.getBlock();

        if (!CROP_CHANCE_MAP.containsKey(block.getType())) {
            return;
        }

        double chance = CROP_CHANCE_MAP.getOrDefault(block.getType(), 0d);

        Random random = new Random();
        double random1 = random.nextDouble();

        if (random1 < chance) {
            account.incrementLevel(SkillType.FARMING);
        }
    }
}
