package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Delete warp command.
 */
public class DeleteWarpCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public DeleteWarpCommand() {
        super("deletewarp");
        setAliases(List.of(
                "delwarp"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!player.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/delwarp 이름").style(MX.STYLE_WARNING));
            return false;
        }

        String key = args[0].toLowerCase();

        if (!Mirae.getState().hasWarp(key)) {
            sender.sendMessage(CANNOT_FIND_WARP);
            return false;
        }

        Mirae.getState().removeWarp(key);
        sender.sendMessage(Component.text("워프가 삭제되었습니다.").style(MX.STYLE_GOOD));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getWarpMap().keySet().stream()
                    .filter(k -> k.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
