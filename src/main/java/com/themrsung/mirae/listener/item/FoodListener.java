package com.themrsung.mirae.listener.item;

import com.themrsung.mirae.item.CustomItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Listens for food events.
 */
public class FoodListener implements Listener {
    /**
     * Constructor.
     */
    public FoodListener() {
        Effects.registerEffects();
    }

    private static final @NotNull Map<CustomItem, Consumer<PlayerItemConsumeEvent>> callbacks = new ConcurrentHashMap<>();

    /**
     * Adds a callback.
     *
     * @param item     The item
     * @param callback The callback
     */
    public static void addCallback(@NotNull CustomItem item, @NotNull Consumer<PlayerItemConsumeEvent> callback) {
        callbacks.put(item, callback);
    }

    @EventHandler
    public void onCustomFoodConsumed(PlayerItemConsumeEvent e) {
        if (e.isCancelled()) return;

        ItemStack item = e.getItem();

        Optional<CustomItem> customItem = callbacks.keySet().stream()
                .filter(i -> i.isItem(item))
                .findAny();

        if (customItem.isEmpty()) return;

        Consumer<PlayerItemConsumeEvent> callback = callbacks.get(customItem.get());
        if (callback == null) return;

        callback.accept(e);
    }

    private static final class Effects {
        private static void registerEffects() {
            addCallback(CustomItem.APPLE_PIE, Effects::onApplePieConsumed);
            addCallback(CustomItem.ARMY_STEW, Effects::onArmyStewConsumed);
            addCallback(CustomItem.HAM, Effects::onHamConsumed);
            addCallback(CustomItem.SALAD, Effects::onSaladConsumed);
            addCallback(CustomItem.SALT_BREAD, Effects::onSaltBreadConsumed);
            addCallback(CustomItem.SAUSAGE, Effects::onSausageConsumed);
            addCallback(CustomItem.ADVANCED_STEAK, Effects::onAdvancedSteakConsumed);
        }

        private static void onApplePieConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(6, EntityRegainHealthEvent.RegainReason.EATING);
        }

        private static void onArmyStewConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(10, EntityRegainHealthEvent.RegainReason.EATING);

            player.setSaturation(player.getSaturation() + 8);
            player.setSaturatedRegenRate(player.getSaturatedRegenRate() + 2);
        }

        private static void onHamConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(2, EntityRegainHealthEvent.RegainReason.EATING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 60, 1));
        }

        private static void onSaladConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(2, EntityRegainHealthEvent.RegainReason.EATING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 2));
        }

        private static void onSaltBreadConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(2, EntityRegainHealthEvent.RegainReason.EATING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1));
        }

        private static void onSausageConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(2, EntityRegainHealthEvent.RegainReason.EATING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 30, 1));
        }

        private static void onAdvancedSteakConsumed(@NotNull PlayerItemConsumeEvent e) {
            Player player = e.getPlayer();
            player.heal(2, EntityRegainHealthEvent.RegainReason.EATING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 60, 1));
        }
    }

    private static final @NotNull EnumSet<EntityType> SALT_DROPPERS = EnumSet.of(
            EntityType.SKELETON,
            EntityType.ZOMBIE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.WITHER_SKELETON,
            EntityType.LLAMA,
            EntityType.TRADER_LLAMA
    );

    @EventHandler
    public void onSaltAcquired(EntityDeathEvent e) {
        if (e.isCancelled()) return;
        if (!SALT_DROPPERS.contains(e.getEntityType())) return;

        Random random = new Random();
        int amount = random.nextInt(6);
        if (amount == 0) return;

        ItemStack salt = CustomItem.SALT.getItem();
        salt.setAmount(amount);

        e.getDrops().add(salt);
    }
}
