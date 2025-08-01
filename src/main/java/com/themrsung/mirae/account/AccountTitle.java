package com.themrsung.mirae.account;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.economy.TitleVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An account title.
 */
public enum AccountTitle {
    EMPTY("empty", Component.empty()),

    CAPITALIST("capitalist", Component.text("자본주의자").style(Style.style()
            .color(TextColor.fromHexString("#4870e0"))
            .decorate(TextDecoration.BOLD)
            .build())),

    NETHER_STAR("nether_star", Component.text(":mc_nether_star:")),

    CLOCK("clock", Component.text(":mc_clock:")),

    RUBY("ruby", Component.text(":mc_ruby:")),

    ELYTRA("elytra", Component.text(":mc_elytra:")),

    BARRIER("barrier", Component.text(":mc_barrier:"));

    AccountTitle(@NotNull String key, @NotNull Component value) {
        this.key = key;
        this.value = value;
    }

    /// Titles

    public static @NotNull AccountTitle getOrEmpty(@Nullable String key) {
        if (key == null) return EMPTY;

        try {
            return valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EMPTY;
        }
    }

    public static @NotNull Set<String> getKeys() {
        return Arrays.stream(values())
                .map(AccountTitle::toString)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());
    }

    /// Body

    private final @NotNull String key;
    private final @NotNull Component value;

    /**
     * Returns the key.
     *
     * @return The key
     */
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    public @NotNull Component getValue() {
        return value;
    }

    /**
     * Generates the title item.
     * @return The title item
     */
    public @NotNull ItemStack generateItem() {
        return generateItem(TitleVersion.CURRENT);
    }

    /**
     * Generates and returns a new account title item.
     *
     * @param version The version
     * @return The item
     */
    public @NotNull ItemStack generateItem(@NotNull TitleVersion version) {
        /// Legacy support here

        assert version == TitleVersion.VERSION_1;

        ItemStack stack = new ItemStack(version.getItem());
        ItemMeta meta = stack.getItemMeta();

        meta.itemName(Component.text(key));
        meta.displayName(value);
        meta.lore(List.of(TitleVersion.VERSION_1_LORE));

        stack.setItemMeta(meta);
        return stack;
    }
}
