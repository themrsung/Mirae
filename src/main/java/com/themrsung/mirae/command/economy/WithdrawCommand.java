package com.themrsung.mirae.command.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.item.economy.Banknote;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Withdraw coin command.
 */
public class WithdrawCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public WithdrawCommand() {
        super("withdraw");
        setAliases(List.of(
                "withdrawmoney",
                "출금",
                "수표"
        ));
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/withdraw 금액").style(MX.STYLE_WARNING));
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        double amount = MX.parseDouble(args[0]);

        if (amount <= 0 || Math.round(amount) != amount) {
            sender.sendMessage(INVALID_AMOUNT);
            return false;
        }

        EconomyResult result = Mirae.getState().withdrawBalance(account, amount, EconomyCause.WITHDRAWN_AS_ITEM);

        if (Objects.equals(result, EconomyResult.FAILURE_INSUFFICIENT_FUNDS)) {
            sender.sendMessage(INSUFFICIENT_FUNDS);
            return false;
        } else if (!result.isSuccess()) {
            sender.sendMessage(INTERNAL_ERROR);
            return false;
        }


        CustomItem banknote = new Banknote(amount);
        ItemStack item = banknote.getItem();

        if (MX.getRemainingSpaceFor(player.getInventory(), item) < 1) {
            sender.sendMessage(INSUFFICIENT_SPACE_IN_INVENTORY);
            return false;
        }

        int remaining = MX.giveItems(player.getInventory(), item);
        if (remaining > 0) {
            ItemStack remainingNote = banknote.getItem();
            remainingNote.setAmount(remaining);

            player.getWorld().dropItem(player.getLocation(), remainingNote);
        }

        double balanceAfter = account.getBalance();
        sender.sendMessage(Component.text("[출금] ").style(MX.STYLE_ERROR)
                .append(Component.text(MX.formatBalance(amount)).style(MX.STYLE_SPECIAL))
                .append(Component.text("을 인출했습니다. 잔액: ").style(MX.STYLE_NORMAL))
                .append(Component.text(MX.formatBalance(balanceAfter)).style(MX.STYLE_SPECIAL)));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
                double balance = account.getBalance();
                yield balance > 0 ? List.of(Long.toString(Math.round(balance))) : List.of("잔고가 없습니다.");
            }
            default -> List.of();
        };
    }
}
