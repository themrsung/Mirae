package com.themrsung.mirae.enchant;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Custom enchantment.
 */
public interface CustomEnchantment {
    /**
     * Returns the map of custom enchantments.
     *
     * @param item The item to check
     * @return The map of enchantments
     */
    static @NotNull Map<CustomEnchantment, Integer> getEnchantments(@Nullable ItemStack item) {
        if (item == null) return Map.of();

        Map<CustomEnchantment, Integer> enchantments = new HashMap<>();

        Arrays.stream(Value.values()).forEach(enchantment -> {
            int level = enchantment.getEnchantLevel(item);
            if (level > 0) {
                enchantments.put(enchantment, level);
            }
        });

        return Map.copyOf(enchantments);
    }

    /**
     * Updates the enchantment lore.
     *
     * @param item The item
     */
    static void updateEnchantmentLore(@NotNull ItemStack item) {
        List<Component> lines = new ArrayList<>();
        getEnchantments(item).forEach((k, v) -> {
            if (v < 1) return;

            lines.add(k.getDisplayName()
                    .appendSpace()
                    .append(Component.text(v))
            );
        });

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>(Objects.requireNonNullElse(meta.lore(), List.of()));

        Arrays.stream(Value.values()).forEach(v -> {
            String enchantmentName = ((TextComponent) v.getDisplayName()).content();
            lore.removeIf(l -> {
                String name = ((TextComponent) l).content();
                return name.contains(enchantmentName);
            });
        });

        lore.addAll(lines);

        meta.lore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Returns the display name.
     *
     * @return The display name
     */
    @NotNull Component getDisplayName();

    /**
     * Returns the natural maximum level.
     *
     * @return The max natural level
     */
    int getNaturalMaxLevel();

    /**
     * Returns whether the given item has this enchantment.
     *
     * @param item The item
     * @return {@code true} if the item has the enchantment
     */
    boolean hasEnchant(@Nullable ItemStack item);

    /**
     * Returns whether the given item has this enchantment.
     *
     * @param item  The item
     * @param level The minimum level
     * @return {@code true} if the item has the enchantment
     */
    boolean hasEnchant(@Nullable ItemStack item, int level);

    /**
     * Returns the enchantment level of the given item.
     *
     * @param item The item
     * @return The level if present, {@code 0} otherwise
     */
    int getEnchantLevel(@Nullable ItemStack item);

    /**
     * Sets the enchantment level of the given item.
     *
     * @param item  The item
     * @param level The level to set to
     */
    void setEnchantLevel(@NotNull ItemStack item, int level);

    /**
     * Increments the item's enchantment level.
     *
     * @param item The item
     */
    void incrementEnchantLevel(@NotNull ItemStack item);

    /**
     * Decrements the item's enchantment level.
     *
     * @param item The item
     */
    void decrementEnchantLevel(@NotNull ItemStack item);

    /**
     * Default implementation.
     */
    enum Value implements CustomEnchantment {
        THREE_BY_THREE_MINING("mirae.enchantment.three_by_three", "3x3 채굴"),
        SUPER_SHOVEL("mirae.enchantment.super_shovel", "자갈 관통"),
        SEEKER_BOW("mirae.enchantment.seeker_bow", "화살 유도"),
        EMP_SHIELD("mirae.enchantment.emp_shield", "전자기파 방패"),
        BOOSTER_BOW("mirae.enchantment.booster_bow", "화살 추진", 3);

        /**
         * Constructor.
         *
         * @param key         The key
         * @param displayName The display name
         */
        Value(@NotNull String key, @NotNull String displayName) {
            this(key, Component.text(displayName).style(MX.STYLE_NORMAL), 1);
        }

        /**
         * Constructor.
         *
         * @param key             The key
         * @param displayName     The display name
         * @param naturalMaxLevel The naturally achievable max level
         */
        Value(@NotNull String key, @NotNull String displayName, int naturalMaxLevel) {
            this(key, Component.text(displayName).style(MX.STYLE_NORMAL), naturalMaxLevel);
        }

        /**
         * Constructor.
         *
         * @param key             The key
         * @param displayName     The display name
         * @param naturalMaxLevel The naturally achievable max level
         */
        Value(@NotNull String key, @NotNull Component displayName, int naturalMaxLevel) {
            this.key = key;
            this.displayName = displayName;
            this.naturalMaxLevel = naturalMaxLevel;
        }

        private final @NotNull String key;
        private final @NotNull Component displayName;
        private final int naturalMaxLevel;

        /**
         * Returns the persistent data container key id.
         *
         * @return The key id
         */
        public @NotNull String getKey() {
            return key;
        }

        @Override
        public @NotNull Component getDisplayName() {
            return displayName;
        }

        @Override
        public int getNaturalMaxLevel() {
            return naturalMaxLevel;
        }

        @Override
        public boolean hasEnchant(@Nullable ItemStack item) {
            return hasEnchant(item, 1);
        }

        @Override
        public boolean hasEnchant(@Nullable ItemStack item, int level) {
            return getEnchantLevel(item) >= level;
        }

        @Override
        public int getEnchantLevel(@Nullable ItemStack item) {
            if (item == null) return 0;

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return 0;

            PersistentDataContainer container = meta.getPersistentDataContainer();

            Integer result = container.get(new NamespacedKey(Mirae.getPlugin(), key), PersistentDataType.INTEGER);
            if (result == null) return 0;

            return result;
        }

        @Override
        public void setEnchantLevel(@NotNull ItemStack item, int level) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();

            NamespacedKey nKey = new NamespacedKey(Mirae.getPlugin(), key);
            container.remove(nKey);

            if (level > 0) {
                container.set(nKey, PersistentDataType.INTEGER, level);
            }

            item.setItemMeta(meta);

            updateEnchantmentLore(item);
        }

        @Override
        public void incrementEnchantLevel(@NotNull ItemStack item) {
            int current = getEnchantLevel(item);
            setEnchantLevel(item, current + 1);
        }

        @Override
        public void decrementEnchantLevel(@NotNull ItemStack item) {
            int current = getEnchantLevel(item);
            setEnchantLevel(item, Math.max(current - 1, 0));
        }
    }
}
