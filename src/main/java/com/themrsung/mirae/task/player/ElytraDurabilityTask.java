package com.themrsung.mirae.task.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Elytra durability task.
 */
public class ElytraDurabilityTask implements Runnable {
    public static final int DURABILITY_PER_RUN = 1;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
                .filter(Player::isGliding)
                .filter(p -> !p.getGameMode().isInvulnerable())
                .forEach(p -> {
                    ItemStack possiblyElytra = p.getInventory().getChestplate();
                    if (possiblyElytra == null || possiblyElytra.getType() != Material.ELYTRA) return;

                    ItemMeta meta = possiblyElytra.getItemMeta();
                    if (!(meta instanceof Damageable damageable)) return;

                    int damage = damageable.getDamage();
                    damageable.setDamage(damage + DURABILITY_PER_RUN);

                    possiblyElytra.setItemMeta(meta);
                    p.getInventory().setChestplate(possiblyElytra);
                });
    }
}
