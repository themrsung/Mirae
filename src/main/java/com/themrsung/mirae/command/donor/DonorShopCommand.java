package com.themrsung.mirae.command.donor;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.donor.DonorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Donor shop command.
 */
public class DonorShopCommand extends MiraeCommand {
    public DonorShopCommand() {
        super("donorshop");
        setAliases(List.of(
                "donatorshop",
                "donationshop",
                "후원상점",
                "후원샵"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        DonorMenu menu = new DonorMenu(player);
        menu.openGUI();
        return true;
    }
}
