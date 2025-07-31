package com.themrsung.mirae.command.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.event.economy.EconomyCause;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PayCommand extends MiraeCommand {
    public PayCommand() {
        super("pay");
        setAliases(List.of(
                "송금",
                "이체",
                "계좌이체"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("/pay 대상 금액").style(MX.STYLE_WARNING));
            return false;
        }

        Account moneySender = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        Account moneyRecipient = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);

        if (moneyRecipient == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (moneyRecipient.isIgnoringAccount(moneySender)) {
            sender.sendMessage(COUNTERPARTY_IS_IGNORING_YOU);
            return false;
        }

        if (moneySender.isIgnoringAccount(moneyRecipient)) {
            sender.sendMessage(YOU_ARE_IGNORING_COUNTERPARTY);
            return false;
        }

        double amount = MX.parseDouble(args[1]);
        if (amount <= 0 || amount != Math.round(amount)) {
            sender.sendMessage(INVALID_AMOUNT);
            return false;
        }

        List<EconomyResult> results = Mirae.getState().transferBalance(moneySender, moneyRecipient, amount, EconomyCause.NATIVE_TRANSFER);
        EconomyResult senderResult = results.getFirst();

        if (senderResult.isSuccess()) {
            sender.sendMessage(Component.text("[출금] ").style(MX.STYLE_SPECIAL)
                    .append(moneyRecipient.getDisplayName(MX.STYLE_SPECIAL)
                            .append(Component.text("님에게 ").style(MX.STYLE_NORMAL))
                            .append(Component.text(MX.formatBalance(amount)).style(MX.STYLE_SPECIAL))
                            .append(Component.text("을 보냈습니다.").style(MX.STYLE_NORMAL))));

            assert results.size() >= 2;
            moneyRecipient.sendMessage(Component.text("[입금] ").style(MX.STYLE_SPECIAL)
                    .append(moneySender.getDisplayName(MX.STYLE_SPECIAL)
                            .append(Component.text("님이 ").style(MX.STYLE_NORMAL))
                            .append(Component.text(MX.formatBalance(amount)).style(MX.STYLE_SPECIAL))
                            .append(Component.text("을 보냈습니다.").style(MX.STYLE_NORMAL))));

            return true;
        } else if (senderResult == EconomyResult.FAILURE_INSUFFICIENT_FUNDS) {
            sender.sendMessage(INSUFFICIENT_FUNDS);
            return false;
        } else {
            sender.sendMessage(INTERNAL_ERROR);
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> List.of("금액");
            default -> List.of();
        };
    }
}
