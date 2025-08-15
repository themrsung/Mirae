package com.themrsung.mirae.listener.item;

import com.themrsung.mirae.item.CustomItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
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
            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.STORMBREAKER, e -> {
            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.CAPTAIN_SHIELD, e -> {
            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });

        callbacks.put(CustomItem.METAL_CLAWS, e -> {
            e.setDamage(25 + (e.isCritical() ? 5 : 0));
        });
    }
}
