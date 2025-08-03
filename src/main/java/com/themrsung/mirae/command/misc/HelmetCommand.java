package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Helmet command.
 */
public class HelmetCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public HelmetCommand() {
        super("helmet");
        setAliases(List.of(
                "hat",
                "cap",
                "모자",
                "머리",
                "투구"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack itemOnHat = player.getInventory().getHelmet();

        player.getInventory().setHelmet(itemInHand);
        player.getInventory().setItemInMainHand(itemOnHat);

        sender.sendMessage(Component.text("새로운 모자가 마음에 들었으면 좋겠습니다!").style(MX.STYLE_GOOD));
        return true;
    }
}
