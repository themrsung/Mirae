package com.themrsung.mirae.command.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.event.social.DirectMessageSentEvent;
import com.themrsung.mirae.social.DirectMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DirectMessageCommand extends MiraeCommand {
    public DirectMessageCommand() {
        super("directmessage");
        setAliases(List.of(
                "dm",
                "whisper",
                "w",
                "tell",
                "t",
                "message",
                "msg",
                "m",
                "mail",
                "디엠",
                "귓말",
                "귓"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("/dm 대상 내용").style(MX.STYLE_WARNING));
            return false;
        }

        Account msgSender = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account msgRecipient = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);

        if (msgRecipient == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (msgRecipient.isIgnoringAccount(msgSender)) {
            sender.sendMessage(COUNTERPARTY_IS_IGNORING_YOU);
            return false;
        }

        if (msgSender.isIgnoringAccount(msgRecipient)) {
            sender.sendMessage(YOU_ARE_IGNORING_COUNTERPARTY);
            return false;
        }

        String[] parts = new String[args.length - 1];
        System.arraycopy(args, 1, parts, 0, parts.length);

        DirectMessage message = DirectMessage.compose(msgSender, msgRecipient, MiniMessage.miniMessage().deserialize(String.join(" ", parts)));

        Bukkit.getServer().getPluginManager().callEvent(new DirectMessageSentEvent(message));
        Mirae.getState().addDirectMessage(message);

        sender.sendMessage(message.asShownToSender());
        msgRecipient.sendMessage(message.asShownToRecipient());

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 0 -> List.of();
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of("메시지를 입력하세요...");
        };
    }
}
