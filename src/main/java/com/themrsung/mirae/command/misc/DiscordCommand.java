package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Discord command.
 */
public class DiscordCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public DiscordCommand() {
        super("discord");
        setAliases(List.of(
                "디스코드",
                "디코"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("[디스코드 바로가기]").style(MX.STYLE_GOOD)
                .hoverEvent(HoverEvent.showText(Component.text("디스코드로 이동합니다...").style(MX.STYLE_NORMAL)))
                .clickEvent(ClickEvent.openUrl("https://discord.gg/ta5qhx5SNH")));

        return true;
    }
}
