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

public enum TitleVersion {
    VERSION_1(1, Material.ENCHANTED_BOOK),

    ;

    public static final @NotNull TitleVersion CURRENT = VERSION_1;

    private static final @NotNull EnumSet<TitleVersion> SUPPORTED_VERSIONS = EnumSet.of(VERSION_1);

    public static @NotNull EnumSet<TitleVersion> getSupportedVersions() {
        return EnumSet.copyOf(SUPPORTED_VERSIONS);
    }

    public static boolean isValidTitle(@Nullable ItemStack item) {
        return TitleVersion.getSupportedVersions().stream().anyMatch(t -> t.isTitle(item));
    }

    TitleVersion(int ordinal, @NotNull Material item) {
        this.ordinal = ordinal;
        this.item = item;
    }

    private final int ordinal;
    private final @NotNull Material item;

    public int getOrdinal() {
        return ordinal;
    }

    public @NotNull Material getItem() {
        return item;
    }

    public static final @NotNull Component VERSION_1_LORE = Component.text("우클릭하여 계정에 추가합니다.")
            .style(Style.style()
                    .color(TextColor.fromHexString("#cccccc"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build());

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
