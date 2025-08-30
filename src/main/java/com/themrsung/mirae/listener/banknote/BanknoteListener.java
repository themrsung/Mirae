package com.themrsung.mirae.listener.banknote;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.banknote.BanknoteQueryResult;
import com.themrsung.mirae.banknote.Banknotes;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Banknote related listeners.
 */
public class BanknoteListener implements Listener {
    @EventHandler
    public void onBanknoteRedeemed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        BanknoteQueryResult query = Banknotes.isBanknote(item);
        if (!query.valid()) return;

        Account account = Mirae.getState().getAccount(player);
        if (account == null) return;

        double amount = query.amount();
        if (!Double.isFinite(amount) || amount <= 0) return;

        int remaining = MX.takeItems(player.getInventory(), item);

        if (remaining > 0) return;

        EconomyResult er = Mirae.getState().depositBalance(account, amount, EconomyCause.ITEM_REDEEMED, "Banknote deposit of " + MX.formatBalance(amount) + ".");

        if (!er.isSuccess()) {
            item.setAmount(1);

            MX.giveItems(player, item);

            return;
        }

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.sendMessage(Component.text("[입금] ").style(MX.STYLE_GOOD)
                .append(Component.text(MX.formatBalance(amount)).style(MX.STYLE_SPECIAL))
                .append(Component.text("을 계좌로 입금하였습니다.").style(MX.STYLE_NORMAL)));

        e.setCancelled(true);
    }
}
