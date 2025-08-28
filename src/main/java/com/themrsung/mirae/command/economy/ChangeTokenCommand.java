package com.themrsung.mirae.command.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EquityToken;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.themrsung.mirae.Mirae.getState;

/**
 * Change token balance command.
 */
public class ChangeTokenCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public ChangeTokenCommand() {
        super("changetoken");
        setAliases(List.of(
                "edittoken"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("/changetoken 대상 토큰 금액").style(MX.STYLE_WARNING));
            return false;
        }

        String query = args[0];
        Optional<Account> optionalAccount = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(query))
                .findAny();

        if (optionalAccount.isEmpty()) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        Account account = optionalAccount.get();
        EquityToken token;

        try {
            token = EquityToken.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("토큰을 찾을 수 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        long change = Math.round(MX.parseDouble(args[2]));

        long after = account.modifyCoinBalance(change, EconomyCause.ADMIN_COMMAND);

        sender.sendMessage(account.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("의 ").style(MX.STYLE_NORMAL))
                .append(token.getDisplayName())
                .append(Component.text(" 잔고를 ").style(MX.STYLE_NORMAL))
                .append(Component.text(MX.formatCoinBalance(after)).style(MX.STYLE_SPECIAL))
                .append(Component.text("으로 설정했습니다.").style(MX.STYLE_NORMAL)));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> Arrays.stream(EquityToken.values())
                    .map(EquityToken::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .toList();
            case 3 -> List.of("금액");
            default -> List.of();
        };
    }
}
