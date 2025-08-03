package com.themrsung.mirae.command.debug;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.MainMenu;
import com.themrsung.mirae.gui.debug.DebugMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Test command.
 */
public class TestCommand extends MiraeCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        DebugMenu menu = new DebugMenu(player);
        menu.openGUI();

        MainMenu menu2 = new MainMenu(player);
        menu2.openGUI();

        return true;
    }
}
