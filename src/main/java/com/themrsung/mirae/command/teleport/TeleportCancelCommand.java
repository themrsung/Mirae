package com.themrsung.mirae.command.teleport;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.social.TeleportRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Teleport Cancel Command.
 */
public class TeleportCancelCommand extends MiraeCommand {
    public TeleportCancelCommand() {
        super("teleportaskcancel");
        setAliases(List.of(
                "teleportcancel",
                "tpacancel",
                "tpcancel",
                "티피취소"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/tpcancel 대상").style(MX.STYLE_WARNING));
            return false;
        }

        Account senderAccount = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account outboundRequestRecipient = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);


        if (outboundRequestRecipient == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (outboundRequestRecipient.getPlayer() == null || !outboundRequestRecipient.getPlayer().isOnline()) {
            sender.sendMessage(COUNTERPARTY_IS_OFFLINE);
            return false;
        }

        if (outboundRequestRecipient.isIgnoringAccount(senderAccount)) {
            sender.sendMessage(COUNTERPARTY_IS_IGNORING_YOU);
            return false;
        }

        if (senderAccount.isIgnoringAccount(outboundRequestRecipient)) {
            sender.sendMessage(YOU_ARE_IGNORING_COUNTERPARTY);
            return false;
        }

        Optional<TeleportRequest> request = Mirae.getState().getTeleportRequests().stream()
                .filter(r -> Objects.equals(r.sender().getUniqueId(), senderAccount.getUniqueId()))
                .filter(r -> Objects.equals(r.recipient().getUniqueId(), outboundRequestRecipient.getUniqueId()))
                .findAny();

        if (request.isEmpty()) {
            sender.sendMessage(NO_OUTBOUND_TELEPORT_REQUESTS);
            return false;
        }

        Player counterparty = outboundRequestRecipient.getPlayer();

        Mirae.getState().removeTeleportRequest(request.get());

        sender.sendMessage(TELEPORT_REQUEST_CANCELLED_BY_SELF);
        counterparty.sendMessage(TELEPORT_REQUEST_CANCELLED_BY_COUNTERPARTY);

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                yield Mirae.getState().getTeleportRequests().stream()
                        .filter(r -> Objects.equals(r.sender().getUniqueId(), player.getUniqueId()))
                        .map(TeleportRequest::recipient)
                        .map(Account::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
            default -> List.of();
        };
    }
}
