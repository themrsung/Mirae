package com.themrsung.mirae.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * The type of skills.
 */
public enum SkillType {
    MINING(Component.text("채굴").style(Style.style().color(TextColor.fromHexString("#56e4ff")).build())),
    FARMING(Component.text("농업").style(Style.style().color(TextColor.fromHexString("#ffe272")).build())),
    CARPENTRY(Component.text("벌목").style(Style.style().color(TextColor.fromHexString("#420b07")).build()));

    SkillType(@NotNull Component displayName) {
        this.displayName = displayName;
    }

    private final @NotNull Component displayName;

    public @NotNull Component getDisplayName() {
        return displayName;
    }
}
