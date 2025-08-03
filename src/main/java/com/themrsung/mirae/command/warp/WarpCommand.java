package com.themrsung.mirae.command.warp;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Delete warp command.
 */
public class WarpCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public WarpCommand() {
        super("warp");
        setAliases(List.of(
                "워프",
                "이동"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/warp 이름").style(MX.STYLE_WARNING));
            return false;
        }

        String key = args[0].toLowerCase();
        Location warp = Mirae.getState().getWarp(key);

        if (warp == null) {
            sender.sendMessage(CANNOT_FIND_WARP);
            return false;
        }

        player.teleport(warp);
        sender.sendMessage(TELEPORTED_SUCCESSFULLY);
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
