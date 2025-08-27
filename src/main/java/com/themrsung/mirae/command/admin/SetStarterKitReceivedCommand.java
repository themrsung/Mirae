package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.themrsung.mirae.Mirae.getState;

/**
 * Set tier command.
 */
public class SetStarterKitReceivedCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetStarterKitReceivedCommand() {
        super("setstarterkitreceived");
        setAliases(List.of(
                "changestarterkitreceived"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("/setstarterkitreceived 대상 true/false").style(MX.STYLE_WARNING));
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
        boolean received = Boolean.parseBoolean(args[1]);

        account.setReceivedStarterKit(received);

        sender.sendMessage(account.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님의 스타터킷 수령 상태를 ").style(MX.STYLE_NORMAL))
                .append(Component.text(received))
                .append(Component.text("으로 설정했습니다.")).style(MX.STYLE_NORMAL));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> Stream.of("true", "false")
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
