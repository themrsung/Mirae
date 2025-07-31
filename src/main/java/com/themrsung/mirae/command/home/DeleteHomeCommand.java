package com.themrsung.mirae.command.home;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeleteHomeCommand extends MiraeCommand {
    public DeleteHomeCommand() {
        super("deletehome");
        setAliases(List.of(
                "delhome",
                "집삭제",
                "홈삭제"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (args.length == 0) {
            account.setHome(null);
            sender.sendMessage(HOME_DELETED);
            return true;
        }

        String key = args[0];

        boolean exists = account.getExtraHomeMap().containsKey(key);

        if (!exists) {
            sender.sendMessage(Component.text("홈이 존재하지 않습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        account.setExtraHome(key, null);
        sender.sendMessage(HOME_DELETED);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
                yield List.copyOf(account.getExtraHomeMap().keySet().stream()
                        .filter(key -> key.startsWith(args[0]))
                        .toList());
            }
            default -> List.of();
        };
    }
}
