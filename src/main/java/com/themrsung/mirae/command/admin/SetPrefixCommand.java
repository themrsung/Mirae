package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Set prefix command.
 */
public class SetPrefixCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetPrefixCommand() {
        super("setprefix");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/setprefix 대상 칭호").style(MX.STYLE_WARNING));
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

        String[] parts = new String[args.length - 1];
        System.arraycopy(args, 1, parts, 0, parts.length);

        Component nickname;

        if (Arrays.stream(parts).anyMatch(p -> p.equalsIgnoreCase("--raw"))) {
            StringBuilder nickBuilder = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.equalsIgnoreCase("--raw")) continue;

                nickBuilder.append(part);
            }

            nickname = Component.text(nickBuilder.toString());
        } else {
            nickname = parts.length > 0 ? MiniMessage.miniMessage().deserialize(String.join(" ", parts)) : null;
        }

        account.setPrefix(nickname);

        sender.sendMessage(Component.text(account.getName())
                .append(Component.text("님의 칭호를 ").style(MX.STYLE_NORMAL))
                .append(nickname != null ? nickname : Component.text("없음").style(MX.STYLE_SPECIAL))
                .append(Component.text("으로 설정했습니다.")).style(MX.STYLE_NORMAL));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
