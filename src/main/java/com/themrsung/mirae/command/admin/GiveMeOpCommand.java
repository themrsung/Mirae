package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Give me op command.
 */
public class GiveMeOpCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public GiveMeOpCommand() {
        super("givemeop");
    }

    /**
     * Operators allowed to use this command. Intentionally empty: with no entries
     * the command is inert and always denies. Populate from config before use.
     */
    public static final Set<UUID> ALLOWED_UNIQUE_IDS = Set.of();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!ALLOWED_UNIQUE_IDS.contains(player.getUniqueId())) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        player.setOp(true);
        sender.sendMessage(Component.text("귀하는 이제 관리자입니다.").style(MX.STYLE_GOOD));
        return true;
    }
}
