package com.themrsung.mirae.command.cooking;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.cooking.CookingMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Cooking command.
 */
public class CookingCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public CookingCommand() {
        super("cooking");
        setAliases(List.of(
                "cook",
                "요리"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        CookingMenu menu = new CookingMenu(player);
        menu.openGUI();
        return true;
    }
}
