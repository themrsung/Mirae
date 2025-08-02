package com.themrsung.mirae.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * The type of skills.
 */
public enum SkillType {
    /**
     * Mining skill.
     */
    MINING(Component.text("채굴").style(Style.style().color(TextColor.fromHexString("#56e4ff")).build())),

    /**
     * Farming skill.
     */
    FARMING(Component.text("농업").style(Style.style().color(TextColor.fromHexString("#ffe272")).build())),

    /**
     * Carpentry skill.
     */
    CARPENTRY(Component.text("벌목").style(Style.style().color(TextColor.fromHexString("#420b07")).build()));

    /**
     * Creates a new skill.
     *
     * @param displayName The display name
     */
    SkillType(@NotNull Component displayName) {
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
