package com.themrsung.mirae.listener.lootbox;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.item.economy.Banknote;
import com.themrsung.mirae.item.lootbox.LootBox;
import com.themrsung.mirae.item.lootbox.LootBoxReward;
import com.themrsung.mirae.item.lootbox.LootBoxReward.BanknoteBased;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * Loot Box redemption listener.
 */
public class LootBoxRedemptionListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLootBoxRedeemed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack item = player.getInventory().getItemInMainHand();
        for (LootBox lootBox : LootBox.BOXES) {
            if (lootBox.isItem(item)) {
                LootBoxReward reward = lootBox.pollReward();

                ItemStack rewardItem = reward.getItem();
                ItemMeta rewardMeta = rewardItem.getItemMeta();

                Component displayName = Objects.requireNonNullElse(rewardMeta.displayName(), Component.text(MX.getKoreanMaterialName(rewardItem.getType())));

                ItemStack itemsToTake = item.clone();
                itemsToTake.setAmount(1);

                int remainingTake = MX.takeItems(player.getInventory(), itemsToTake);
                if (remainingTake > 0) {
                    return;
                }

                MX.giveItems(player, rewardItem);

                if (reward instanceof BanknoteBased banknoteReward) {
                    Banknote.Version version = banknoteReward.getVersion();
                    if (version.tracksIssuance()) {
                        Mirae.getState().adjustTrackedBanknoteIssuance(banknoteReward.getDenomination());
                    }
                }

                if (reward.getRarity().shouldBroadcast()) {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.playSound(p, reward.getRarity().getSound(), 1, 1);
                        p.sendMessage(account.getDisplayName(MX.STYLE_SPECIAL)
                                .append(Component.text("님이 랜덤박스로 [").style(MX.STYLE_NORMAL))
                                .append(displayName.hoverEvent(rewardItem.asHoverEvent()))
                                .append(Component.text("]을 획득했습니다!").style(MX.STYLE_NORMAL)));
                    });

                } else {
                    player.playSound(player, reward.getRarity().getSound(), 1, 1);
                }

                e.setCancelled(true);
                return;
            }
        }
    }
}
