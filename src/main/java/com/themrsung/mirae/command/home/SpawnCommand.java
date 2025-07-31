package com.themrsung.mirae.command.home;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Spawn command.
 */
public class SpawnCommand extends MiraeCommand {
    /**
     * Default constructor.
     */
    public SpawnCommand() {
        super("spawn");
        setAliases(List.of(
                "스폰"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Location spawn = Mirae.getState().getSpawnPoint();
        if (spawn == null) {
            sender.sendMessage(INTERNAL_ERROR);
            return false;
        }

        player.teleport(spawn);

        sender.sendMessage(TELEPORTED_SUCCESSFULLY);
        return true;
    }
}
