package com.themrsung.mirae.market;

import com.themrsung.mirae.MX;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * The category of a market.
 */
public enum MarketCategory {
    /**
     * Default category.
     */
    NONE(Component.text("(분류 없음)").style(MX.STYLE_NORMAL)),

    /**
     * Agriculture.
     */
    AGRICULTURE(Component.text("농산물").style(MX.STYLE_NORMAL)),

    /**
     * Raw materials.
     */
    RAW_MATERIALS(Component.text("원자재").style(MX.STYLE_NORMAL)),

    /**
     * Timber.
     */
    TIMBER(Component.text("목재").style(MX.STYLE_NORMAL)),

    /**
     * Seafood.
     */
    SEAFOOD(Component.text("해산물").style(MX.STYLE_NORMAL)),

    /**
     * Foods.
     */
    FOODS(Component.text("식품").style(MX.STYLE_NORMAL)),

    /**
     * Basic blocks.
     */
    BASIC_BLOCKS(Component.text("블럭 (기본)").style(MX.STYLE_NORMAL)),

    /**
     * Building blocks.
     */
    BUILDING_BLOCKS(Component.text("블럭 (건축)").style(MX.STYLE_NORMAL)),

    /**
     * Nether blocks.
     */
    NETHER_BLOCKS(Component.text("블럭 (네더)").style(MX.STYLE_NORMAL)),

    /**
     * End blocks.
     */
    END_BLOCKS(Component.text("블럭 (엔더)").style(MX.STYLE_NORMAL)),

    /**
     * Lighting blocks.
     */
    LIGHTING_BLOCKS(Component.text("블럭 (조명)").style(MX.STYLE_NORMAL)),

    /**
     * Custom items.
     */
    CUSTOM_ITEMS(Component.text("커스텀 아이템").style(MX.STYLE_NORMAL)),

    /**
     * Luxury items.
     */
    LUXURY_ITEMS(Component.text("명품").style(MX.STYLE_NORMAL)),

    /**
     * Speculative goods.
     */
    SPECULATIVE_GOODS(Component.text("투기재").style(MX.STYLE_NORMAL)),

    ;

    MarketCategory(@NotNull Component displayName) {
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
}
