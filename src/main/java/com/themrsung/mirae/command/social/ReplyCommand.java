package com.themrsung.mirae.command.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.event.social.DirectMessageSentEvent;
import com.themrsung.mirae.social.DirectMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Reply command.
 */
public class ReplyCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public ReplyCommand() {
        super("reply");
        setAliases(List.of(
                "r",
                "답장",
                "답변"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("/reply [유저] 내용").style(MX.STYLE_WARNING));
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            // No player designated

            String message = String.join(" ", args);

            DirectMessage recent = Mirae.getState().getDirectMessages().stream()
                    .filter(dm -> Objects.equals(dm.recipient().getUniqueId(), player.getUniqueId()))
                    .max(Comparator.comparing(DirectMessage::time))
                    .orElse(null);

            if (recent == null) {
                sender.sendMessage(NO_INBOUND_DIRECT_MESSAGES);
                return false;
            }

            DirectMessage reply = DirectMessage.compose(account, recent.sender(), Component.text(message));

            Bukkit.getServer().getPluginManager().callEvent(new DirectMessageSentEvent(reply));
            Mirae.getState().addDirectMessage(reply);

            sender.sendMessage(reply.asShownToSender());
            recent.sender().sendMessage(reply.asShownToRecipient());

            return true;
        }

        // Player designated

        Account targetAccount = MX.requireAccountNonNull(Mirae.getState().getAccount(targetPlayer));

        String[] parts = new String[args.length - 1];
        System.arraycopy(args, 1, parts, 0, parts.length);

        String message = String.join(" ", parts);

        DirectMessage recent = Mirae.getState().getDirectMessages().stream()
                .filter(dm -> Objects.equals(dm.recipient().getUniqueId(), player.getUniqueId()))
                .filter(dm -> Objects.equals(dm.sender().getUniqueId(), targetAccount.getUniqueId()))
                .max(Comparator.comparing(DirectMessage::time))
                .orElse(null);

        if (recent == null) {
            sender.sendMessage(NO_INBOUND_DIRECT_MESSAGES);
            return false;
        }

        DirectMessage reply = DirectMessage.compose(account, recent.sender(), Component.text(message));

        Bukkit.getServer().getPluginManager().callEvent(new DirectMessageSentEvent(reply));
        Mirae.getState().addDirectMessage(reply);

        sender.sendMessage(reply.asShownToSender());
        recent.sender().sendMessage(reply.asShownToRecipient());

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 0 -> List.of();
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                yield Mirae.getState().getDirectMessages().stream()
                        .filter(dm -> Objects.equals(dm.recipient().getUniqueId(), player.getUniqueId()))
                        .map(DirectMessage::sender)
                        .map(Account::getName)
                        .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
            default -> List.of("메시지를 입력하세요...");
        };
    }
}
