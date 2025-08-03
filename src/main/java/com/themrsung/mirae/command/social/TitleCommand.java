package com.themrsung.mirae.command.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Title command.
 */
public class TitleCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public TitleCommand() {
        super("title");
        setAliases(List.of(
                "changetitle",
                "selecttitle",
                "choosetitle",
                "칭호",
                "칭호설정",
                "칭호선택"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (args.length < 1) {
            account.setCurrentTitle(AccountTitle.EMPTY);
            sender.sendMessage(Component.text("기본 칭호로 변경했습니다.").style(MX.STYLE_GOOD));
            return true;
        }

        AccountTitle title = AccountTitle.getOrEmpty(args[0].toLowerCase());
        if (!account.hasTitle(title)) {
            sender.sendMessage(Component.text("해당 칭호를 보유하고 있지 않습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        account.setCurrentTitle(title);
        sender.sendMessage(Component.text("칭호를 \"").style(MX.STYLE_NORMAL)
                .append(title.getValue())
                .append(Component.text("\"으로 설정했습니다.").style(MX.STYLE_NORMAL)));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();

                Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
                yield account.getTitleSet().stream()
                        .map(AccountTitle::getKey)
                        .map(String::toLowerCase)
                        .filter(s -> s.startsWith(args[0].toLowerCase()))
                        .toList();
            }
            default -> List.of();
        };
    }
}
