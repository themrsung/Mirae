package com.themrsung.mirae.command.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.economy.CoinVersion;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Withdraw coin command.
 */
public class CoinCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public CoinCommand() {
        super("coin");
        setAliases(List.of(
                "코인"
        ));
    }

    public static @NotNull ItemStack getCoinItem() {
        return getCoinItem(CoinVersion.CURRENT);
    }

    public static @NotNull ItemStack getCoinItem(@NotNull CoinVersion version) {

        /// Legacy support here

        assert version == CoinVersion.VERSION_1;

        CustomStack stack = CustomStack.getInstance("iageneric:coin");
        if (stack == null) {
            throw new RuntimeException("Unable to get coin item.");
        }

        ItemStack item = stack.getItemStack();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<gradient:green:gold>후원 코인<reset>")
                .style(Style.style().decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).build());

        meta.itemName(name);
        meta.displayName(name);
        meta.lore(List.of(
                Component.text("후원을 통해 얻을 수 있습니다.").style(MX.STYLE_NORMAL)
        ));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            Bukkit.dispatchCommand(sender, "balance");
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        long amount = Math.round(MX.parseDouble(args[0]));

        if (amount <= 0 || amount > 36 * 64) {
            sender.sendMessage(INVALID_AMOUNT);
            return false;
        }

        EconomyResult result = Mirae.getState().withdrawCoinBalance(account, amount, EconomyCause.WITHDRAWN_AS_ITEM);

        if (Objects.equals(result, EconomyResult.FAILURE_INSUFFICIENT_FUNDS)) {
            sender.sendMessage(INSUFFICIENT_FUNDS);
            return false;
        } else if (!result.isSuccess()) {
            sender.sendMessage(INTERNAL_ERROR);
            return false;
        }

        ItemStack coins = getCoinItem();
        coins.setAmount((int) amount);

        if (MX.getRemainingSpaceFor(player.getInventory(), coins) < amount) {
            sender.sendMessage(INSUFFICIENT_SPACE_IN_INVENTORY);
            return false;
        }

        int remaining = MX.giveItems(player.getInventory(), coins);
        if (remaining > 0) {
            ItemStack remainingCoins = getCoinItem();
            remainingCoins.setAmount(remaining);

            player.getWorld().dropItem(player.getLocation(), remainingCoins);
        }

        long coinsAfter = account.getCoinBalance();
        sender.sendMessage(Component.text(MX.formatCoinBalance(amount)).style(MX.STYLE_SPECIAL)
                .append(Component.text("을 인출했습니다. 잔액: ").style(MX.STYLE_NORMAL))
                .append(Component.text(MX.formatCoinBalance(coinsAfter)).style(MX.STYLE_SPECIAL)));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                if (!(sender instanceof Player player)) yield List.of();
                Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
                long coins = account.getCoinBalance();
                yield coins > 0 ? List.of(Long.toString(coins)) : List.of("보유한 코인이 없습니다.");
            }
            default -> List.of();
        };
    }
}
