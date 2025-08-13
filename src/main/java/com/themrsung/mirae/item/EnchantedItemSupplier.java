package com.themrsung.mirae.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Enchanted item supplier.
 */
public class EnchantedItemSupplier implements ItemSupplier {
    /// ENCHANTMENTS

    /**
     * Pickaxe enchantments.
     */
    private static final @NotNull Map<Enchantment, Integer> PICKAXE_ENCHANTMENTS = Map.of(
            Enchantment.EFFICIENCY, 5,
            Enchantment.UNBREAKING, 3,
            Enchantment.FORTUNE, 3,
            Enchantment.MENDING, 1
    );

    /**
     * Sword enchantments.
     */
    private static final @NotNull Map<Enchantment, Integer> SWORD_ENCHANTMENTS = Map.of(
            Enchantment.SHARPNESS, 5,
            Enchantment.UNBREAKING, 3,
            Enchantment.SMITE, 5,
            Enchantment.FIRE_ASPECT, 2,
            Enchantment.SWEEPING_EDGE, 3,
            Enchantment.LOOTING, 3,
            Enchantment.MENDING, 1
    );

    /**
     * Axe enchantments.
     */
    private static final @NotNull Map<Enchantment, Integer> AXE_ENCHANTMENTS = Map.of(
            Enchantment.SHARPNESS, 5,
            Enchantment.UNBREAKING, 3,
            Enchantment.SMITE, 5,
            Enchantment.LOOTING, 3,
            Enchantment.FORTUNE, 3,
            Enchantment.FIRE_ASPECT, 2,
            Enchantment.EFFICIENCY, 5,
            Enchantment.MENDING, 1
    );

    /**
     * Bow enchantments.
     */
    private static final @NotNull Map<Enchantment, Integer> BOW_ENCHANTMENTS = Map.of(
            Enchantment.POWER, 5,
            Enchantment.PUNCH, 5,
            Enchantment.FLAME, 1,
            Enchantment.UNBREAKING, 3,
            Enchantment.MENDING, 1,
            Enchantment.INFINITY, 1
    );

    /**
     * Mending book.
     */
    private static final @NotNull Map<Enchantment, Integer> MENDING = Map.of(
            Enchantment.MENDING, 1
    );

    /// ITEMS

    /**
     * Enchanted netherite pickaxe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_NETHERITE_PICKAXE = new EnchantedItemSupplier(new ItemStack(Material.NETHERITE_PICKAXE), PICKAXE_ENCHANTMENTS);

    /**
     * Enchanted diamond pickaxe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_DIAMOND_PICKAXE = new EnchantedItemSupplier(new ItemStack(Material.DIAMOND_PICKAXE), PICKAXE_ENCHANTMENTS);

    /**
     * Enchanted iron pickaxe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_IRON_PICKAXE = new EnchantedItemSupplier(new ItemStack(Material.IRON_PICKAXE), PICKAXE_ENCHANTMENTS);

    /**
     * Enchanted netherite sword.
     */
    public static final @NotNull ItemSupplier ENCHANTED_NETHERITE_SWORD = new EnchantedItemSupplier(new ItemStack(Material.NETHERITE_SWORD), SWORD_ENCHANTMENTS);

    /**
     * Enchanted diamond sword.
     */
    public static final @NotNull ItemSupplier ENCHANTED_DIAMOND_SWORD = new EnchantedItemSupplier(new ItemStack(Material.DIAMOND_SWORD), SWORD_ENCHANTMENTS);

    /**
     * Enchanted iron sword.
     */
    public static final @NotNull ItemSupplier ENCHANTED_IRON_SWORD = new EnchantedItemSupplier(new ItemStack(Material.IRON_SWORD), SWORD_ENCHANTMENTS);

    /**
     * Enchanted netherite axe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_NETHERITE_AXE = new EnchantedItemSupplier(new ItemStack(Material.NETHERITE_AXE), AXE_ENCHANTMENTS);

    /**
     * Enchanted diamond axe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_DIAMOND_AXE = new EnchantedItemSupplier(new ItemStack(Material.DIAMOND_AXE), AXE_ENCHANTMENTS);

    /**
     * Enchanted iron axe.
     */
    public static final @NotNull ItemSupplier ENCHANTED_IRON_AXE = new EnchantedItemSupplier(new ItemStack(Material.IRON_AXE), AXE_ENCHANTMENTS);

    /**
     * Enchanted bow.
     */
    public static final @NotNull ItemSupplier ENCHANTED_BOW = new EnchantedItemSupplier(new ItemStack(Material.BOW), BOW_ENCHANTMENTS);

    /**
     * Mending book.
     */
    public static final @NotNull ItemSupplier MENDING_BOOK = new EnchantedItemSupplier(new ItemStack(Material.ENCHANTED_BOOK), MENDING);

    /**
     * Creates a new supplier.
     *
     * @param i        The item
     * @param levelMap The level map
     */
    public EnchantedItemSupplier(@NotNull ItemStack i, @NotNull Map<Enchantment, Integer> levelMap) {
        this.item = i.clone();

        ItemMeta meta = item.getItemMeta();

        levelMap.forEach((e, l) -> {
            meta.addEnchant(e, l, true);
        });

        item.setItemMeta(meta);
    }

    private final @NotNull ItemStack item;

    @Override
    public @NotNull ItemStack getItem() {
        return item.clone();
    }
}
