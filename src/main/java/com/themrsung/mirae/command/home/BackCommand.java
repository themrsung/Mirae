package com.themrsung.mirae.command.home;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

/**
 * Back command.
 */
public class BackCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public BackCommand() {
        super("back");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (args.length > 0 && args[0].equalsIgnoreCase("death")) {
            Location lastDeath = account.getRecentDeathLocation();
            if (lastDeath == null) {
                sender.sendMessage(Component.text("최근에 사망한 지점이 없습니다.").style(MX.STYLE_WARNING));
                return false;
            }

            player.teleport(lastDeath);
            sender.sendMessage(TELEPORTED_SUCCESSFULLY);
            return true;
        }

        Location lastDeparture = account.getRecentTeleportDeparture();
        if (lastDeparture == null) {
            sender.sendMessage(Component.text("최근에 텔레포트한 지점이 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        player.teleport(lastDeparture);
        sender.sendMessage(TELEPORTED_SUCCESSFULLY);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Stream.of("death")
                    .filter(arg -> arg.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
