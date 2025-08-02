package com.themrsung.mirae.account;

import com.themrsung.mirae.MX;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
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
    /**
     * Default tier.
     */
    DEFAULT(0, Component.empty(), Component.empty()),

    /**
     * Green tier.
     */
    GREEN(1, Component.text("그린").style(Style.style()
            .color(TextColor.fromHexString("#3cee7d"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build()),
            Component.text("누적후원금액 1만원 이상").style(MX.STYLE_NORMAL)),

    /**
     * Gold tier.
     */
    GOLD(2, Component.text("골드").style(Style.style()
            .color(TextColor.fromHexString("#ecd731"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build()),
            Component.text("누적후원금액 5만원 이상").style(MX.STYLE_NORMAL)),

    /**
     * Platinum tier.
     */
    PLATINUM(3, Component.text("플래티넘").style(Style.style()
            .color(TextColor.fromHexString("#c3c2ab"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build()),
            Component.text("누적후원금액 50만원 이상").style(MX.STYLE_NORMAL)),

    /**
     * Black tier.
     */
    BLACK(4, Component.text("블랙").style(Style.style()
            .color(TextColor.fromHexString("#130e0b"))
            .decorate(TextDecoration.BOLD)
            .decoration(TextDecoration.ITALIC, false)
            .build()),
            Component.text("누적후원금액 100만원 이상").style(MX.STYLE_NORMAL)),

    /**
     * Developer tier.
     */
    DEVELOPER(100, MiniMessage.miniMessage().deserialize("<gradient:#ff2e01:#2e2727>developer<reset>"),
            MiniMessage.miniMessage().deserialize("<gradient:white:gold><bold>누적개발시간 300시간 이상"));

    /**
     * Creates a new tier.
     *
     * @param ordinal     The constant ordinal
     * @param displayName The display name
     * @param description The description
     */
    AccountTier(int ordinal, @NotNull Component displayName, @NotNull Component description) {
        this.ordinal = ordinal;
        this.displayName = displayName.hoverEvent(HoverEvent.showText(description));
        this.description = description;
    }

    private final int ordinal;
    private final @NotNull Component displayName;
    private final @NotNull Component description;

    /**
     * Returns the display name.
     *
     * @return The display name
     */
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    /**
     * Returns the description.
     *
     * @return The description
     */
    public @NotNull Component getDescription() {
        return description;
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

    /**
     * Returns if this tier is at least the given tier.
     *
     * @param t The tier
     * @return {@code true} if this tier is at least the given tier
     */
    public boolean isAtLeast(@Nullable AccountTier t) {
        if (t == null) return false;
        return ordinal >= t.ordinal;
    }
}
