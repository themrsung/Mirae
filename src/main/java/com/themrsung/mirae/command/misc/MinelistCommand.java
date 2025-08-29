package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Vote command.
 */
public class MinelistCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public MinelistCommand() {
        super("minelist");
        setAliases(List.of(
                "vote",
                "추천",
                "마인리스트",
                "마인리스트추천"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("마인리스트 추천하기: ").style(MX.STYLE_NORMAL)
                .append(Component.text("[클릭]").style(MX.STYLE_GOOD))
                .hoverEvent(Component.text("클릭하여 추천합니다..."))
                .clickEvent(ClickEvent.openUrl("https://minelist.kr/servers/16551-themrsung.com/votes/new")));
        return true;
    }
}
