package com.themrsung.mirae.command.teleport;

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

/**
 * Home command.
 */
public class HomeCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public HomeCommand() {
        super("home");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (args.length > 0) {
            String key = args[0];
            Location home = account.getExtraHome(key);

            if (home == null) {
                sender.sendMessage(Component.text("집을 찾을 수 없습니다.").style(MX.STYLE_WARNING));
                return false;
            }

            player.teleport(home);
            sender.sendMessage(TELEPORTED_SUCCESSFULLY);
            return true;
        }

        Location home = account.getHome();

        if (home == null) {
            sender.sendMessage(Component.text("집을 설정하지 않았습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        player.teleport(home);
        sender.sendMessage(TELEPORTED_SUCCESSFULLY);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
                yield List.copyOf(account.getExtraHomeMap().keySet());
            }
            default -> List.of();
        };
    }
}
