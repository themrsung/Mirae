package com.themrsung.mirae.task.item;

import com.themrsung.mirae.enchant.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

/**
 * Arrow seeker task.
 */
public class EmpShieldTask implements Runnable {
    public static final double EMP_RANGE = 3;
    public static final double DISTRACT_VECTOR_MULTIPLIER = 5;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ItemStack possiblyShield = player.getInventory().getItemInOffHand();
            if (!CustomEnchantment.Value.EMP_SHIELD.hasEnchant(possiblyShield)) return;
            if (possiblyShield.getType() == Material.ENCHANTED_BOOK) return;

            player.getNearbyEntities(EMP_RANGE, EMP_RANGE, EMP_RANGE).stream()
                    .filter(e -> e instanceof Projectile)
                    .map(e -> (Projectile) e)
                    .filter(p -> !Objects.equals(p.getShooter(), player))
                    .forEach(projectile -> {
                        Random random = new Random();

                        Vector v = projectile.getVelocity();
                        Vector r = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5)
                                .normalize()
                                .multiply(0.1);
                        Vector d = projectile.getLocation().toVector()
                                .subtract(player.getLocation().toVector())
                                .add(r); // Add randomness
                        Vector p = v.clone().add(d.clone().multiply(DISTRACT_VECTOR_MULTIPLIER));

                        Vector n = p.clone().normalize();
                        Vector m = n.clone().multiply(v.length());

                        projectile.setVelocity(m);
                    });
        });
    }
}
