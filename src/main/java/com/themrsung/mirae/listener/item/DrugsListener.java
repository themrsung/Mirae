package com.themrsung.mirae.listener.item;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Drugs listener.
 */
public class DrugsListener implements Listener {
    @EventHandler
    public void onDrugUsed(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (CustomItem.COCAINE.isItem(item)) {
            ItemStack cocaine = CustomItem.COCAINE.getItem();
            cocaine.setAmount(1);

            int failed = MX.takeItems(player.getInventory(), cocaine);
            if (failed > 0) return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 180 * 20, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 120 * 20, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 180 * 20, 5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 360 * 20, 1));

            player.playSound(player, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 1);
            e.setCancelled(true);
        } else if (CustomItem.CRACK_COCAINE.isItem(item)) {
            ItemStack crack = CustomItem.CRACK_COCAINE.getItem();
            crack.setAmount(1);

            int failed = MX.takeItems(player.getInventory(), crack);
            if (failed > 0) return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60 * 20, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 30 * 20, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 120 * 20, 2));

            player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
            e.setCancelled(true);
        } else if (CustomItem.HEROIN.isItem(item)) {
            ItemStack heroin = CustomItem.HEROIN.getItem();
            heroin.setAmount(1);

            int failed = MX.takeItems(player.getInventory(), heroin);
            if (failed > 0) return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 420 * 20, 1));

            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, (float) 1.25);
            e.setCancelled(true);
        } else if (CustomItem.MORPHINE.isItem(item)) {
            ItemStack morphine = CustomItem.MORPHINE.getItem();
            morphine.setAmount(1);

            int failed = MX.takeItems(player.getInventory(), morphine);
            if (failed > 0) return;

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 180 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 360 * 20, 1));


            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, (float) 1.75);
            e.setCancelled(true);
        }
    }
}
