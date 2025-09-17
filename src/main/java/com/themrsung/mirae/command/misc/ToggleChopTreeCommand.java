package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Command that toggles access to the tree chopper feature.
 */
public class ToggleChopTreeCommand extends MiraeCommand {
    private static final @NotNull String TREE_CHOPPER_PERMISSION = "treechopper.use";

    /**
     * Creates a new command.
     */
    public ToggleChopTreeCommand() {
        super("togglechoptree");
        setAliases(List.of(
                "choptreetoggle",
                "treetoggle",
                "toggletree",
                "찹트리설정",
                "찹트리"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        RegisteredServiceProvider<Permission> provider = Mirae.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        if (provider == null) {
            sender.sendMessage(INTERNAL_ERROR);
            Mirae.getInstance().getLogger().severe("Vault permission provider is not registered.");
            return false;
        }

        Permission permission = provider.getProvider();
        String worldName = player.getWorld().getName();
        boolean currentlyEnabled = permission.playerHas(worldName, player, TREE_CHOPPER_PERMISSION);

        boolean success;
        if (currentlyEnabled) {
            success = permission.playerRemove(worldName, player, TREE_CHOPPER_PERMISSION);
            if (!success) {
                sender.sendMessage(INTERNAL_ERROR);
                Mirae.getInstance().getLogger().severe("Failed to revoke tree chopper permission from " + player.getName());
                return false;
            }

            sender.sendMessage(Component.text("찹트리를 사용하지 않습니다.").style(MX.STYLE_GOOD));
        } else {
            success = permission.playerAdd(worldName, player, TREE_CHOPPER_PERMISSION);
            if (!success) {
                sender.sendMessage(INTERNAL_ERROR);
                Mirae.getInstance().getLogger().severe("Failed to grant tree chopper permission to " + player.getName());
                return false;
            }

            sender.sendMessage(Component.text("찹트리를 사용합니다.").style(MX.STYLE_GOOD));
        }

        return true;
    }
}
