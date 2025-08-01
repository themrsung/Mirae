package com.themrsung.mirae.command.teleport;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.social.TeleportRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * TPA command.
 */
public class TeleportAskCommand extends MiraeCommand {
    public TeleportAskCommand() {
        super("teleportask");
        setAliases(List.of(
                "tpa",
                "티피에이",
                "티피"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/tpa 대상").style(MX.STYLE_WARNING));
            return false;
        }

        Account tpSender = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account tpRecipient = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);


        if (tpRecipient == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (Objects.equals(tpSender.getUniqueId(), tpRecipient.getUniqueId())) {
            sender.sendMessage(CANNOT_DO_THIS_TO_SELF);
            return false;
        }

        if (tpRecipient.getPlayer() == null || !tpRecipient.getPlayer().isOnline()) {
            sender.sendMessage(COUNTERPARTY_IS_OFFLINE);
            return false;
        }

        if (tpRecipient.isIgnoringAccount(tpSender)) {
            sender.sendMessage(COUNTERPARTY_IS_IGNORING_YOU);
            return false;
        }

        if (tpSender.isIgnoringAccount(tpRecipient)) {
            sender.sendMessage(YOU_ARE_IGNORING_COUNTERPARTY);
            return false;
        }

        if (Mirae.getState().getTeleportRequests().stream()
                .filter(tr -> Objects.equals(tr.sender().getUniqueId(), tpSender.getUniqueId()))
                .anyMatch(tr -> Objects.equals(tr.recipient().getUniqueId(), tpRecipient.getUniqueId()))
        ) {
            sender.sendMessage(OUTBOUND_TELEPORT_REQUEST_ALREADY_EXISTS);
            return false;
        }

        TeleportRequest request = TeleportRequest.createRequest(tpSender, tpRecipient, false);
        Mirae.getState().addTeleportRequest(request);

        sender.sendMessage(tpRecipient.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님에게 텔레포트 요청을 보냈습니다.").style(MX.STYLE_NORMAL))
                .appendNewline()
                .append(Component.text("[취소]").style(MX.STYLE_GOOD).clickEvent(ClickEvent.runCommand("/tpcancel " + tpRecipient.getName()))));

        tpRecipient.sendMessage(tpSender.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님이 텔레포트 요청을 보냈습니다.").style(MX.STYLE_NORMAL))
                .appendNewline()
                .append(Component.text("[수락]").style(MX.STYLE_GOOD).clickEvent(ClickEvent.runCommand("/tpaccept " + tpSender.getName())))
                .append(Component.text(" | ").style(MX.STYLE_NORMAL))
                .append(Component.text("[거절]").style(MX.STYLE_ERROR).clickEvent(ClickEvent.runCommand("/tpdeny " + tpSender.getName()))));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
