package com.themrsung.mirae.listener.enchant;

import com.themrsung.mirae.enchant.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

/**
 * Super shovel listener.
 */
public class SuperShovelListener implements Listener {
    private static final @NotNull EnumSet<Material> TARGET_BLOCKS = EnumSet.of(
            Material.GRASS_BLOCK,
            Material.DIRT,
            Material.DIRT_PATH,
            Material.ROOTED_DIRT,
            Material.COARSE_DIRT,
            Material.FARMLAND,
            Material.SAND,
            Material.RED_SAND,
            Material.SUSPICIOUS_SAND,
            Material.GRAVEL,
            Material.SUSPICIOUS_GRAVEL,
            Material.PODZOL,
            Material.MYCELIUM
    );

    @EventHandler
    public void onSuperShovelUsed(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().isLeftClick()) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        boolean isSuperShovel = CustomEnchantment.Value.SUPER_SHOVEL.hasEnchant(item);

        if (!isSuperShovel) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (!TARGET_BLOCKS.contains(block.getType())) return;

        player.breakBlock(block);
        e.setCancelled(true);
    }
}
