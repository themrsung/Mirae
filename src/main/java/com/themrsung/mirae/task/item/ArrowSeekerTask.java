package com.themrsung.mirae.task.item;

import com.themrsung.mirae.enchant.CustomEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * Arrow seeker task.
 */
public class ArrowSeekerTask implements Runnable {
    public static final double SEEKER_ACTIVATION_DISTANCE = 7;
    private static final double SEEKER_ACTIVATION_DISTANCE_SQUARED = SEEKER_ACTIVATION_DISTANCE * SEEKER_ACTIVATION_DISTANCE;

    public static final double SEEKER_SEEK_DISTANCE = 7;
    public static final double SEEKER_SEEK_VELOCITY = 3;

    public static final double SEEKER_TERMINATION_VELOCITY = 0.01;
    private static final double SEEKER_TERMINATION_VELOCITY_SQUARED = SEEKER_TERMINATION_VELOCITY * SEEKER_TERMINATION_VELOCITY;

    @Override
    public void run() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(e -> e instanceof Projectile)
                .map(e -> (Projectile) e)
                .filter(p -> p.getVelocity().lengthSquared() > SEEKER_TERMINATION_VELOCITY_SQUARED)
                .filter(p -> p.getType() == EntityType.ARROW || p.getType() == EntityType.SPECTRAL_ARROW)
                .forEach(arrow -> {
                    ProjectileSource shooter = arrow.getShooter();
                    if (!(shooter instanceof Player player)) return;

                    ItemStack possiblyBow = player.getInventory().getItemInMainHand();
                    if (!CustomEnchantment.Value.SEEKER_BOW.hasEnchant(possiblyBow)) return;
                    if (possiblyBow.getType() == Material.ENCHANTED_BOOK) return;

                    Vector playerPos = player.getLocation().toVector();
                    Vector arrowPos = arrow.getLocation().toVector();
                    Vector playerArrowDiff = arrowPos.clone().subtract(playerPos);

                    if (playerArrowDiff.lengthSquared() < SEEKER_ACTIVATION_DISTANCE_SQUARED) return;

                    Vector possiblyTarget = arrow.getNearbyEntities(SEEKER_SEEK_DISTANCE, SEEKER_SEEK_DISTANCE, SEEKER_SEEK_DISTANCE).stream()
                            .filter(e -> !Objects.equals(e, arrow))
                            .filter(e -> !Objects.equals(e, shooter))
                            .filter(e -> e.getType().isAlive() || // Targets
                                    e.getType() == EntityType.PLAYER || // Targets
                                    e.getType() == EntityType.ITEM || // Chaff
                                    e.getType() == EntityType.FIREWORK_ROCKET || // Flares
                                    e.getType() == EntityType.END_CRYSTAL)

                            .min((e1, e2) -> {
                                double d1 = e1.getLocation().distanceSquared(arrow.getLocation());
                                double d2 = e2.getLocation().distanceSquared(arrow.getLocation());

                                return Double.compare(d1, d2);
                            })
                            .map(Entity::getLocation)
                            .map(Location::toVector)
                            .orElse(null);

                    if (possiblyTarget == null) return;

                    int boosterLevel = CustomEnchantment.Value.BOOSTER_BOW.getEnchantLevel(possiblyBow);
                    Vector directionToTarget = possiblyTarget.clone().subtract(arrowPos).multiply(SEEKER_SEEK_VELOCITY * (boosterLevel + 1));

                    Vector velocityBefore = arrow.getVelocity();
                    Vector sum = velocityBefore.clone().add(directionToTarget);

                    Vector normalizedSum = sum.clone().normalize();
                    Vector velocityAfter = normalizedSum.clone().multiply(velocityBefore.length());

                    arrow.setVelocity(velocityAfter);
                }));
    }
}
