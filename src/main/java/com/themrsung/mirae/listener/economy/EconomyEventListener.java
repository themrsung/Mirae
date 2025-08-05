package com.themrsung.mirae.listener.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.TitleVersion;
import com.themrsung.mirae.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Listens for economy events.
 */
public final class EconomyEventListener implements Listener {
    @EventHandler
    public void onCoinRedeemed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack item = e.getItem();
        if (item == null || !CustomItem.DONOR_COIN.isItem(item)) return;

        e.setCancelled(true);

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

    @EventHandler
    public void onTitleRedeemed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack item = e.getItem();
        if (item == null || !TitleVersion.isValidTitle(item)) return;

        e.setCancelled(true);

        ItemMeta meta = item.getItemMeta();
        Component itemName = meta.itemName();
        String key = ((TextComponent) itemName).content();

        AccountTitle title = AccountTitle.getOrEmpty(key);
        if (title == AccountTitle.EMPTY) {
            player.sendMessage(Component.text("칭호를 획득하는 데 오류가 발생했습니다. 관리자에게 문의하세요.").style((MX.STYLE_ERROR)));
            return;
        }

        if (account.hasTitle(title)) {
            player.sendMessage(Component.text("이미 해당 칭호를 보유하고 있습니다.").style(MX.STYLE_WARNING));
            return;
        }

        if (MX.takeItems(player.getInventory(), item) > 0) {
            player.sendMessage(Component.text("칭호를 계정으로 추가하는 데 오류가 발생했습니다. 관리자에게 문의하세요.").style((MX.STYLE_ERROR)));
            return;
        }

        account.addTitle(title);
        player.sendMessage(Component.text("칭호 \"").style(MX.STYLE_NORMAL)
                .append(title.getValue())
                .append(Component.text("\"을 획득했습니다.").style(MX.STYLE_NORMAL)));
    }
}
