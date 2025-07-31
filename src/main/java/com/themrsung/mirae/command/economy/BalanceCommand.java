package com.themrsung.mirae.command.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BalanceCommand extends MiraeCommand {
    public BalanceCommand() {
        super("balance");
        setAliases(List.of(
                "bal",
                "money",
                "cash",
                "돈",
                "잔고",
                "잔액"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof ConsoleCommandSender || (sender.isOp() && args.length > 0)) {
            String query = args[0];
            List<Account> accounts = Mirae.getState().getAccounts().stream()
                    .filter(a -> a.getName().contains(query))
                    .toList();

            if (accounts.isEmpty()) {
                sender.sendMessage(CANNOT_FIND_ACCOUNT);
                return false;
            }

            for (Account account : accounts) {
                if (account.getName().equalsIgnoreCase(query)) {
                    sendBalanceInformation(sender, account);
                    return true;
                }
            }

            for (Account account : accounts) {
                if (account.getName().toLowerCase().startsWith(query.toLowerCase())) {
                    sendBalanceInformation(sender, account);
                    return true;
                }
            }

            sendBalanceInformation(sender, accounts.getFirst());
            return true;
        }

        assert sender instanceof Player;

        Player player = (Player) sender;
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        sendBalanceInformation(sender, account);

        return true;
    }

    private void sendBalanceInformation(@NotNull CommandSender sender, @NotNull Account account) {
        double balance = Math.round(account.getWallet().getBalance());
        long coinBalance = account.getWallet().getCoinBalance();

        sender.sendMessage(Component.text("[은행] 계좌 정보").style(MX.STYLE_SPECIAL));
        sender.sendMessage(Component.text("  - 계좌 잔액: ").style(MX.STYLE_NORMAL)
                .append(Component.text(MX.formatBalance(balance)).style(MX.STYLE_SPECIAL)));
        sender.sendMessage(Component.text("  - 후원 코인: ").style(MX.STYLE_NORMAL)
                .append(Component.text(MX.formatCoinBalance(coinBalance)).style(MX.STYLE_SPECIAL)));
        sender.sendMessage(Component.text("  - 경제 상태: ").style(MX.STYLE_NORMAL)
                .append(Mirae.getState().isEconomyFrozen() ? Component.text("동결").style(MX.STYLE_ERROR) : Component.text("정상").style(MX.STYLE_GOOD)));
        sender.sendMessage(Component.text("  - 계좌 상태: ").style(MX.STYLE_NORMAL)
                .append(account.isWalletFrozen() ? Component.text("동결").style(MX.STYLE_ERROR) : Component.text("정상").style(MX.STYLE_GOOD)));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if (!sender.isOp()) return List.of();

        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
