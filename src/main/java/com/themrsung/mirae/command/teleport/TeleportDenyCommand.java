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

public class TeleportDenyCommand extends MiraeCommand {
    public TeleportDenyCommand() {
        super("teleportdeny");
        setAliases(List.of(
                "teleportno",
                "tpdeny",
                "tpno",
                "티피거절"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/tpdeny 대상").style(MX.STYLE_WARNING));
            return false;
        }

        Account senderAccount = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account inboundRequestSender = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);


        if (inboundRequestSender == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (inboundRequestSender.getPlayer() == null || !inboundRequestSender.getPlayer().isOnline()) {
            sender.sendMessage(COUNTERPARTY_IS_OFFLINE);
            return false;
        }

        if (inboundRequestSender.isIgnoringAccount(senderAccount)) {
            sender.sendMessage(COUNTERPARTY_IS_IGNORING_YOU);
            return false;
        }

        if (senderAccount.isIgnoringAccount(inboundRequestSender)) {
            sender.sendMessage(YOU_ARE_IGNORING_COUNTERPARTY);
            return false;
        }

        Optional<TeleportRequest> request = Mirae.getState().getTeleportRequests().stream()
                .filter(r -> Objects.equals(r.recipient().getUniqueId(), senderAccount.getUniqueId()))
                .filter(r -> Objects.equals(r.sender().getUniqueId(), inboundRequestSender.getUniqueId()))
                .findAny();

        if (request.isEmpty()) {
            sender.sendMessage(NO_INBOUND_TELEPORT_REQUESTS);
            return false;
        }

        Player counterparty = inboundRequestSender.getPlayer();

        Mirae.getState().removeTeleportRequest(request.get());

        player.sendMessage(TELEPORT_REQUEST_DENIED);
        counterparty.sendMessage(COUNTERPARTY_HAS_DENIED_TELEPORT_REQUEST);

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                yield Mirae.getState().getTeleportRequests().stream()
                        .filter(r -> Objects.equals(r.recipient().getUniqueId(), player.getUniqueId()))
                        .map(TeleportRequest::sender)
                        .map(Account::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
            default -> List.of();
        };
    }
}
