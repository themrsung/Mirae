package com.themrsung.mirae.item.lootbox;

import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.item.EnchantedItemSupplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Loot box.
 */
public interface LootBox extends CustomItem {
    /// BOXES

    /**
     * Test box.
     */
    @NotNull LootBox TEST_BOX = ItemsAdderLootBox.builder()
            .instanceId("crystals:purple_crystal")
            .displayName(Component.text("테스트 박스"))
            .reward(LootBoxReward.fromSupplier(1, MYTHIC_HAMMER, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, STORM_HAMMER, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, HERO_SHIELD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, METAL_CLAWS, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, BLUE_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, RED_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, GREEN_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, BASEBALL_BAT, LootBoxReward.Rarity.LEGENDARY))
            .build();

    /**
     * Purple loot box.
     */
    @NotNull LootBox PURPLE_BOX = ItemsAdderLootBox.builder()
            .instanceId("crystals:purple_crystal")
            .displayName(Component.text("퍼플 박스").style(Style.style()
                    .color(TextColor.fromHexString("#a64ea4"))
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .build()))
            .reward(LootBoxReward.fromSupplier(1, MYTHIC_HAMMER, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, STORM_HAMMER, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, HERO_SHIELD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, METAL_CLAWS, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, BLUE_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, RED_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, GREEN_BEAM_SWORD, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(1, BASEBALL_BAT, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromItem(15, Material.NETHER_STAR, 2, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromItem(30, Material.NETHERITE_BLOCK, 64, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromBanknote(100, 10000000, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromItem(250, Material.ENCHANTED_GOLDEN_APPLE, 64, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromBanknote(1000, 4500000, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromBanknote(2500, 3000000, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(3500, Material.NETHERITE_BLOCK, 32, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(6000, Material.EMERALD_BLOCK, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(7500, Material.DIAMOND_BLOCK, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromBanknote(9000, 1250000, LootBoxReward.Rarity.DEFAULT))
            .build();

    /**
     * Red loot box.
     */
    @NotNull LootBox RED_BOX = ItemsAdderLootBox.builder()
            .instanceId("crystals:red_crystal")
            .displayName(Component.text("레드 박스").style(Style.style()
                    .color(TextColor.fromHexString("#b02f10"))
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .build()))
            .reward(LootBoxReward.fromSupplier(1, PURPLE_BOX, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(5, EnchantedItemSupplier.ENCHANTED_NETHERITE_PICKAXE, LootBoxReward.Rarity.LEGENDARY))
            .reward(LootBoxReward.fromSupplier(75, EnchantedItemSupplier.ENCHANTED_DIAMOND_PICKAXE, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromSupplier(75, EnchantedItemSupplier.ENCHANTED_DIAMOND_AXE, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromSupplier(75, EnchantedItemSupplier.ENCHANTED_DIAMOND_SWORD, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromBanknote(1000, 750000, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(3500, Material.DIAMOND, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(5000, Material.EMERALD, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromSupplier(7500, EnchantedItemSupplier.MENDING_BOOK, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromBanknote(10000, 250000, LootBoxReward.Rarity.DEFAULT))
            .build();

    /**
     * Orange loot box.
     */
    @NotNull LootBox ORANGE_BOX = ItemsAdderLootBox.builder()
            .instanceId("crystals:orange_crystal")
            .displayName(Component.text("오렌지 박스").style(Style.style()
                    .color(TextColor.fromHexString("#b4942a"))
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .build()))
            .reward(LootBoxReward.fromSupplier(1, RED_BOX, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromSupplier(50, EnchantedItemSupplier.ENCHANTED_IRON_PICKAXE, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromSupplier(50, EnchantedItemSupplier.ENCHANTED_IRON_AXE, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromSupplier(50, EnchantedItemSupplier.ENCHANTED_IRON_SWORD, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromSupplier(50, EnchantedItemSupplier.ENCHANTED_BOW, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromSupplier(150, EnchantedItemSupplier.MENDING_BOOK, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(500, Material.DIAMOND, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(500, Material.EMERALD, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(500, Material.EXPERIENCE_BOTTLE, 32, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromBanknote(1000, 50000, LootBoxReward.Rarity.DEFAULT))
            .build();

    /**
     * Green loot box.
     */
    @NotNull LootBox GREEN_BOX = ItemsAdderLootBox.builder()
            .instanceId("crystals:green_crystal")
            .displayName(Component.text("그린 박스").style(Style.style()
                    .color(TextColor.fromHexString("#3fdf73"))
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .build()))
            .reward(LootBoxReward.fromSupplier(1, ORANGE_BOX, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromSupplier(3, EnchantedItemSupplier.MENDING_BOOK, LootBoxReward.Rarity.RARE))
            .reward(LootBoxReward.fromBanknote(25, 50000, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(50, Material.DIAMOND, 32, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(100, Material.EMERALD, 32, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(200, Material.LAPIS_LAZULI, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromItem(500, Material.COAL, 64, LootBoxReward.Rarity.DEFAULT))
            .reward(LootBoxReward.fromBanknote(1000, 10000, LootBoxReward.Rarity.DEFAULT))
            .build();

    /**
     * The title loot box.
     */
    @NotNull LootBox TITLE_BOX = ItemsAdderLootBox.builder()
            .instanceId("iageneric:golden_key")
            .displayName(Component.text("칭호 박스").style(Style.style()
                    .color(TextColor.fromHexString("#8350df"))
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
                    .build()))
            .rewards(AccountTitle.getAcquirableTitles().stream()
                    .map(title -> LootBoxReward.fromSupplier(1, title::generateItem, LootBoxReward.Rarity.DEFAULT))
                    .toList())
            .build();

    /**
     * The set of boxes.
     */
    @NotNull Set<LootBox> BOXES = Set.of(
            TEST_BOX,
            PURPLE_BOX,
            RED_BOX,
            ORANGE_BOX,
            GREEN_BOX,
            TITLE_BOX
    );

    /// BODY

    /**
     * Returns the display name of this loot box.
     *
     * @return The display name
     */
    @NotNull Component getDisplayName();

    /**
     * Returns the list of potential rewards.
     *
     * @return The list of potential rewards
     */
    @NotNull List<LootBoxReward> getRewards();

    /**
     * Returns the total chance denominator.
     *
     * @return The total chance denominator
     */
    long getTotalDenominator();

    /**
     * Returns the probability of the reward from this loot box.
     *
     * @param reward The reward
     * @return The probability
     */
    double getProbabilityOf(@Nullable LootBoxReward reward);

    /**
     * Polls this box for rewards, then returns one.
     *
     * @return The polled reward
     */
    @NotNull LootBoxReward pollReward();
}
