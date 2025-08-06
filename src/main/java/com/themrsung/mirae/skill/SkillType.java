package com.themrsung.mirae.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
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
    MINING(Component.text("채굴").style(Style.style()
            .hoverEvent(HoverEvent.showText(Component.text("광질을 통해 높일 수 있습니다. 희귀한 광물일수록 유리합니다.")))
            .color(TextColor.fromHexString("#56e4ff")).build())),

    /**
     * Farming skill.
     */
    FARMING(Component.text("농업").style(Style.style()
            .hoverEvent(HoverEvent.showText(Component.text("농사를 통해 높일 수 있습니다.")))
            .color(TextColor.fromHexString("#ffe272")).build())),

    /**
     * Carpentry skill.
     */
    CARPENTRY(Component.text("벌목").style(Style.style()
            .hoverEvent(HoverEvent.showText(Component.text("벌목을 통해 높일 수 있습니다.")))
            .color(TextColor.fromHexString("#420b07")).build())),

    /**
     * Trading skill.
     */
    TRADING(Component.text("무역").style(Style.style()
            .hoverEvent(HoverEvent.showText(Component.text("상거래를 통해 높일 수 있습니다. 상점보다 유저 직거래가 유리합니다.")))
            .color(TextColor.fromHexString("#ff9752")).build())),

    /**
     * Upgrading skill.
     */
    UPGRADING(Component.text("강화").style(Style.style()
            .hoverEvent(HoverEvent.showText(Component.text("강화를 통해 높일 수 있습니다. 강화 티켓 사용 시 레벨이 올라가지 않습니다.")))
            .color(TextColor.fromHexString("#5fdd96")).build())),

    ;

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
