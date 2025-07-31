package com.themrsung.mirae.command.teleport;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Set spawn command.
 */
public class SetSpawnCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetSpawnCommand() {
        super("setspawn");
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

        Location location = player.getLocation();
        Mirae.getState().setSpawnPoint(location);

        sender.sendMessage(Component.text("스폰 지점이 설정되었습니다.").style(MX.STYLE_GOOD));
        return true;
    }
}
