package com.themrsung.mirae.task.item;

import com.themrsung.mirae.enchant.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

/**
 * Arrow propulsion task.
 */
public class ArrowPropulsionTask implements Runnable {

    public static final double PROPULSION_TERMINATION_VELOCITY = 0.01;
    private static final double PROPULSION_TERMINATION_VELOCITY_SQUARED = PROPULSION_TERMINATION_VELOCITY * PROPULSION_TERMINATION_VELOCITY;

    public static final double ACCELERATION = 0.5;
    public static final double MAX_SPEED = 50;

    @Override
    public void run() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(e -> e instanceof Projectile)
                .map(e -> (Projectile) e)
                .filter(p -> p.getVelocity().lengthSquared() > PROPULSION_TERMINATION_VELOCITY_SQUARED)
                .filter(p -> p.getType() == EntityType.ARROW || p.getType() == EntityType.SPECTRAL_ARROW)
                .forEach(arrow -> {
                    ProjectileSource shooter = arrow.getShooter();
                    if (!(shooter instanceof Player player)) return;

                    ItemStack possiblyBow = player.getInventory().getItemInMainHand();
                    int level = CustomEnchantment.Value.BOOSTER_BOW.getEnchantLevel(possiblyBow);

                    if (level < 1) return;
                    if (possiblyBow.getType() == Material.ENCHANTED_BOOK) return;

                    Vector v = arrow.getVelocity();
                    double speed = v.length();

                    Vector p = v.normalize().multiply(Math.min(speed + ACCELERATION * level, MAX_SPEED));
                    arrow.setVelocity(p);
                }));
    }
}
