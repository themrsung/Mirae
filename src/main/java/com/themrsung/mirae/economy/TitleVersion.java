package com.themrsung.mirae.economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

/**
 * Title item version.
 */
public enum TitleVersion {
    /**
     * Created 2025-08-02. Uses ordinary Minecraft enchanted book.
     */
    VERSION_1(1, Material.ENCHANTED_BOOK),

    ;

    /**
     * The current version.
     */
    public static final @NotNull TitleVersion CURRENT = VERSION_1;

    /**
     * Version 1 lore.
     */
    public static final @NotNull Component VERSION_1_LORE = Component.text("우클릭하여 계정에 추가합니다.")
            .style(Style.style()
                    .color(TextColor.fromHexString("#cccccc"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build());

    /**
     * The set of current versions.
     */
    private static final @NotNull EnumSet<TitleVersion> SUPPORTED_VERSIONS = EnumSet.of(VERSION_1);

    /**
     * Returns the set of supported versions.
     *
     * @return The set of supported versions
     */
    public static @NotNull EnumSet<TitleVersion> getSupportedVersions() {
        return EnumSet.copyOf(SUPPORTED_VERSIONS);
    }

    /**
     * Returns whether the given item is a valid title item.
     *
     * @param item The item to check
     * @return {@code true} if valid
     */
    public static boolean isValidTitle(@Nullable ItemStack item) {
        return TitleVersion.getSupportedVersions().stream().anyMatch(t -> t.isTitle(item));
    }

    /**
     * Creates a new title version.
     *
     * @param ordinal  The constant ordinal
     * @param itemType The item type
     */
    TitleVersion(int ordinal, @NotNull Material itemType) {
        this.ordinal = ordinal;
        this.itemType = itemType;
    }

    private final int ordinal;
    private final @NotNull Material itemType;

    /**
     * Returns the constant ordinal.
     *
     * @return The constant ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Returns the item type.
     *
     * @return The item type
     */
    public @NotNull Material getItemType() {
        return itemType;
    }


    /**
     * Returns whether the given item is a title of this version.
     *
     * @param item The item to check
     * @return {@code true} if it is a title
     */
    public boolean isTitle(@Nullable ItemStack item) {
        if (item == null) return false;

        return switch (this) {
            case VERSION_1 -> {
                boolean isEnchantedBook = item.getType() == Material.ENCHANTED_BOOK;

                ItemMeta meta = item.getItemMeta();
                List<Component> lore = meta.lore();

                boolean hasLore = lore != null && lore.contains(VERSION_1_LORE);

                yield isEnchantedBook && hasLore;
            }
        };
    }
}
