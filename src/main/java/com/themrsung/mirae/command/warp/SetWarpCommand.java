package com.themrsung.mirae.command.warp;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Set warp command.
 */
public class SetWarpCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetWarpCommand() {
        super("setwarp");
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
            sender.sendMessage(Component.text("/setwarp 이름").style(MX.STYLE_WARNING));
            return false;
        }

        String key = args[0].toLowerCase();
        Location value = player.getLocation();

        Mirae.getState().setWarp(key, value);
        sender.sendMessage(Component.text("워프가 설정되었습니다!").style(MX.STYLE_GOOD));
        return true;
    }
}
