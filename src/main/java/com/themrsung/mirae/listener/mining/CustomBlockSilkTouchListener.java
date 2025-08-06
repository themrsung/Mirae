package com.themrsung.mirae.listener.mining;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom block silk touch mining listener.
 */
public class CustomBlockSilkTouchListener implements Listener {
    @EventHandler
    public void onCustomBreak(CustomBlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemMeta meta = tool.getItemMeta();

        if (meta == null) return;
        if (!meta.hasEnchant(Enchantment.SILK_TOUCH)) return;

        player.getWorld().dropItem(e.getBlock().getLocation(), e.getCustomBlockItem());
    }
}
