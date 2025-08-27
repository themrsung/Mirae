package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Take title.
 */
public class TakeTitleCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public TakeTitleCommand() {
        super("taketitle");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("/taketitle 대상 칭호").style(MX.STYLE_WARNING));
            return false;
        }

        Account account;

        try {
            account = Mirae.getState().getAccounts().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                    .findAny()
                    .orElseThrow(RuntimeException::new);
        } catch (RuntimeException e) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        AccountTitle title;

        try {
            title = AccountTitle.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("칭호를 찾을 수 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        if (!account.hasTitle(title)) {
            sender.sendMessage(Component.text("유저가 해당 칭호를 보유하고 있지 않습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        account.removeTitle(title);
        sender.sendMessage(Component.text("칭호를 삭제했습니다.").style(MX.STYLE_GOOD));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();

            case 2 -> Mirae.getState().getAccounts().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                    .findAny()
                    .map(Account::getTitleSet).stream()
                    .flatMap(Collection::stream)
                    .map(AccountTitle::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .toList();

            default -> List.of();
        };
    }
}
