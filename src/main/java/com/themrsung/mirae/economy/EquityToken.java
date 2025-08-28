package com.themrsung.mirae.economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

/**
 * Equity token.
 */
public enum EquityToken {
    GAMMA("GMA", Component.text("감마 토큰").style(Style.style()
            .color(TextColor.fromHexString("#ffffff"))
            .decoration(TextDecoration.ITALIC, false)
            .decorate(TextDecoration.BOLD)
            .build()));

    ;

    /**
     * Creates a new equity token.
     * @param symbol The symbol
     * @param displayName The display name
     */
    EquityToken(@NotNull String symbol, @NotNull Component displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    private final @NotNull String symbol;
    private final @NotNull Component displayName;

    /**
     * Returns the symbol.
     * @return The symbol
     */
    public @NotNull String getSymbol() {
        return symbol;
    }

    /**
     * Returns the display name.
     * @return The display name
     */
    public @NotNull Component getDisplayName() {
        return displayName;
    }
}
