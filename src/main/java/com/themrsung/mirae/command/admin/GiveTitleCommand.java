package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Give title.
 */
public class GiveTitleCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public GiveTitleCommand() {
        super("givetitle");
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

        if (account.hasTitle(title)) {
            sender.sendMessage(Component.text("유저가 이미 칭호를 보유하고 있습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        sender.sendMessage(Component.text("칭호를 지급했습니다.").style(MX.STYLE_GOOD));
        account.addTitle(title);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> Arrays.stream(AccountTitle.values())
                    .map(AccountTitle::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
