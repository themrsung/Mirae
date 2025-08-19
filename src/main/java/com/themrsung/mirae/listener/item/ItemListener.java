package com.themrsung.mirae.listener.item;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.item.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Custom items listener.
 */
public class ItemListener implements Listener {
    private static final @NotNull Map<CustomItem, Consumer<EntityDamageByEntityEvent>> callbacks = new ConcurrentHashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        Entity damager = e.getDamager();
        if (!(damager instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        Optional<CustomItem> customItem = callbacks.keySet().stream()
                .filter(c -> c.isItem(item))
                .findAny();

        if (customItem.isEmpty()) return;

        Consumer<EntityDamageByEntityEvent> callback = callbacks.get(customItem.get());
        callback.accept(e);
    }

    static {
        callbacks.put(CustomItem.THOR_HAMMER, e -> {
            Entity entity = e.getEntity();
            entity.setFireTicks(entity.getFireTicks() + 10);

            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.STORMBREAKER, e -> {
            Entity entity = e.getEntity();
            entity.setFireTicks(entity.getFireTicks() + 10);

            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.CAPTAIN_SHIELD, e -> {
            Entity entity = e.getEntity();
            entity.setFireTicks(entity.getFireTicks() + 10);

            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.METAL_CLAWS, e -> {
            Entity entity = e.getEntity();
            entity.setFireTicks(entity.getFireTicks() + 10);

            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        Consumer<EntityDamageByEntityEvent> lightsaberCallback = e -> {
            Entity entity = e.getEntity();
            entity.setFireTicks(entity.getFireTicks() + 1200);

            e.setDamage(10 + (e.isCritical() ? 2.5 : 0));
        };

        callbacks.put(CustomItem.BLUE_LIGHTSABER, lightsaberCallback);
        callbacks.put(CustomItem.RED_LIGHTSABER, lightsaberCallback);
        callbacks.put(CustomItem.GREEN_LIGHTSABER, lightsaberCallback);

        callbacks.put(CustomItem.BASEBALL_BAT, e -> {
            Entity entity = e.getEntity();

            Bukkit.getScheduler().scheduleSyncDelayedTask(Mirae.getInstance(), () -> {
                Vector up = new Vector(0, 2.5, 0);
                Vector v = entity.getVelocity();
                Vector p = v.add(up);

                entity.setVelocity(p);
            }, 1);
        });
    }

    private final @NotNull Set<CustomItem> THOR_PROPELLANTS = Set.of(
            CustomItem.THOR_HAMMER,
            CustomItem.STORMBREAKER
    );

    private final @NotNull Map<UUID, LocalTime> RECENT_PROPULSION_MAP = new ConcurrentHashMap<>();

    public void clearPropulsionMap() {
        RECENT_PROPULSION_MAP.clear();
    }

    @EventHandler
    public void onThorPropulsion(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.isGliding()) return;

        if (!e.getAction().isRightClick()) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (THOR_PROPELLANTS.stream().noneMatch(p -> p.isItem(item))) return;

        UUID uniqueId = player.getUniqueId();
        LocalTime now = LocalTime.now();
        LocalTime cutoff = now.minusSeconds(1);
        LocalTime recent = RECENT_PROPULSION_MAP.getOrDefault(uniqueId, cutoff);

        if (recent.isAfter(cutoff)) return;

        e.setCancelled(true);

        Vector a = player.getEyeLocation()
                .getDirection()
                .normalize()
                .multiply(7.5);

        Vector v = player.getVelocity();
        Vector p = v.add(a);

        player.setVelocity(p);
        player.setGliding(true);

        player.playSound(player, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 1);

        RECENT_PROPULSION_MAP.put(uniqueId, LocalTime.now());
    }
}
