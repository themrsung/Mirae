package com.themrsung.mirae.command.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Ignore command.
 */
public class IgnoreCommand extends MiraeCommand {
    public IgnoreCommand() {
        super("ignore");
        setAliases(List.of(
                "차단",
                "무시"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/ignore 대상").style(MX.STYLE_WARNING));
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account accountToIgnore = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);

        if (accountToIgnore == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (Objects.equals(account.getUniqueId(), accountToIgnore.getUniqueId())) {
            sender.sendMessage(CANNOT_DO_THIS_TO_SELF);
            return false;
        }

        if (args.length < 2 || args[1].equalsIgnoreCase("toggle")) {
            boolean ignoring = account.isIgnoringAccount(accountToIgnore);
            account.setIgnoringAccount(accountToIgnore, !ignoring);

            sender.sendMessage(accountToIgnore.getDisplayName(MX.STYLE_SPECIAL)
                    .append(Component.text("님에 대한 차단 상태를 변경했습니다. 현재 상태: ").style(MX.STYLE_NORMAL))
                    .append(!ignoring ? Component.text("차단 중").style(MX.STYLE_ERROR) : Component.text("차단 안함").style(MX.STYLE_GOOD)));
            return true;
        }

        boolean shouldIgnore = !args[1].equalsIgnoreCase("false");
        account.setIgnoringAccount(accountToIgnore, shouldIgnore);

        sender.sendMessage(accountToIgnore.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님에 대한 차단 상태를 변경했습니다. 현재 상태: ").style(MX.STYLE_NORMAL))
                .append(shouldIgnore ? Component.text("차단 중").style(MX.STYLE_ERROR) : Component.text("차단 안함").style(MX.STYLE_GOOD)));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> List.of("true", "false", "toggle");
            default -> List.of();
        };
    }
}
