package com.themrsung.mirae.command.warp;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Warp list command.
 */
public class WarpsCommand extends MiraeCommand {
    public WarpsCommand() {
        super("warps");
        setAliases(List.of(
                "warplist",
                "워프목록",
                "워프리스트"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("워프 목록:").style(MX.STYLE_SPECIAL));
        Mirae.getState().getWarpMap().forEach((k, v) -> {
            String location = MX.locationToReadableString(v);
            Component message = Component.text("  - " + k + ": ").style(MX.STYLE_NORMAL)
                    .append(Component.text(location).style(MX.STYLE_SPECIAL))
                    .hoverEvent(HoverEvent.showText(Component.text("클릭하여 이동합니다...").style(MX.STYLE_NORMAL)))
                    .clickEvent(ClickEvent.runCommand("/warp " + k));

            sender.sendMessage(message);
        });

        return true;
    }
}
