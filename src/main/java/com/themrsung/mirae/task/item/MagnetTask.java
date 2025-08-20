package com.themrsung.mirae.task.item;

import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * Magnet task.
 */
public class MagnetTask implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            ItemStack possiblyMagnet = p.getInventory().getItemInOffHand();
            if (!CustomItem.MAGNET.isItem(possiblyMagnet)) return;

            p.getNearbyEntities(3, 3, 3).stream()
                    .filter(e -> e.getType() == EntityType.ITEM)
                    .forEach(i -> i.teleport(p));
        });
    }
}
