package com.themrsung.mirae.account;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Account tier.
 */
public enum AccountTier {
    DEFAULT(Component.empty()),

    GREEN(Component.text("그린").style(Style.style()
            .color(TextColor.fromHexString("#3cee7d"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build())),

    GOLD(Component.text("골드").style(Style.style()
            .color(TextColor.fromHexString("#ecd731"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build())),

    PLATINUM(Component.text("플래티넘").style(Style.style()
            .color(TextColor.fromHexString("#c3c2ab"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build())),


    BLACK(Component.text("블랙").style(Style.style()
            .color(TextColor.fromHexString("#130e0b"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build())),

    DEVELOPER(MiniMessage.miniMessage()
            .deserialize("<gradient:#ff2e01:#2e2727>developer<reset>"));

    AccountTier(@NotNull Component displayName) {
        this.displayName = displayName;
    }

    private final @NotNull Component displayName;

    /**
     * Returns the display name.
     *
     * @return The display name
     */
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    /**
     * Returns whether there is a next tier.
     *
     * @return {@code true} if there is a next tier
     */
    public boolean hasNext() {
        return this != BLACK;
    }

    /**
     * Returns the next tier.
     *
     * @return The next tier
     */
    public @Nullable AccountTier next() {
        return switch (this) {
            case DEFAULT -> GREEN;
            case GREEN -> GOLD;
            case GOLD -> PLATINUM;
            case PLATINUM -> BLACK;
            default -> null;
        };
    }

    /**
     * Returns whether there is a previous tier.
     *
     * @return {@code true} if there is a previous tier
     */
    public boolean hasPrevious() {
        return this != DEFAULT;
    }

    /**
     * Returns the previous tier.
     *
     * @return The previous tier
     */
    public @Nullable AccountTier previous() {
        return switch (this) {
            case BLACK -> PLATINUM;
            case PLATINUM -> GOLD;
            case GOLD -> GREEN;
            case GREEN -> DEFAULT;
            default -> null;
        };
    }
}
