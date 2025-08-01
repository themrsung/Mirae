package com.themrsung.mirae.listener.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.CoinVersion;
import com.themrsung.mirae.event.economy.EconomyCause;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class EconomyEventListener implements Listener {
    @EventHandler
    public void onCoinRedeemed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack item = e.getItem();
        if (item == null || !CoinVersion.isValidCoin(item)) return;

        int quantity = player.isSneaking() ? item.getAmount() : 1;

        ItemStack itemsToTake = item.clone();
        itemsToTake.setAmount(quantity);

        int remaining = MX.takeItems(player.getInventory(), itemsToTake);
        if (remaining > 0) {
            quantity -= remaining;
        }

        long coinsAfter = account.modifyCoinBalance(quantity, EconomyCause.ITEM_REDEEMED);

        player.sendMessage(Component.text(MX.formatCoinBalance(quantity)).style(MX.STYLE_SPECIAL)
                .append(Component.text("을 입금했습니다. 잔액: ").style(MX.STYLE_NORMAL))
                .append(Component.text(MX.formatCoinBalance(coinsAfter)).style(MX.STYLE_SPECIAL)));

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }
}
