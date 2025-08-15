package com.themrsung.mirae.listener.item;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.item.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Custom items listener.
 */
public class ItemListener implements Listener {
    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null) return;

        ItemMeta meta = item.getItemMeta();

        if (!(meta instanceof Damageable damageable)) return;

        int maxDamage = item.getType().getMaxDurability();
        int damage = damageable.getDamage();

        double entropy = (double) damage / maxDamage;
        if (entropy > 0.99) {
            e.setCancelled(true);

            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            player.sendMessage(Component.text("아이템 내구도가 1% 미만입니다.").style(MX.STYLE_ERROR));
        } else if (entropy > 0.95) {
            player.sendMessage(Component.text("아이템 내구도가 5% 미만입니다.").style(MX.STYLE_WARNING));
        } else if (entropy > 0.9) {
            player.sendMessage(Component.text("아이템 내구도가 10% 미만입니다.").style(MX.STYLE_WARNING));
        }
    }

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
    }
}
