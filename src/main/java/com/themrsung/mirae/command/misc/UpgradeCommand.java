package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.upgrade.UpgradeMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Upgrade command.
 */
public class UpgradeCommand extends MiraeCommand {
    /**
     * Creates a new upgrade command.
     */
    public UpgradeCommand() {
        super("upgrade");
        setAliases(List.of("강화"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        UpgradeMenu menu = new UpgradeMenu(player);
        menu.openGUI();
        return true;
    }
}
