package com.themrsung.mirae.listener.economy;

import com.themrsung.mirae.Mirae;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Random;

/**
 * Limits farmable mobs when players are offline.
 */
public class FarmableMobSpawnListener implements Listener {
    private static final EnumSet<EntityType> FARMABLE_MOBS = EnumSet.of(
            EntityType.ZOMBIE,
            EntityType.CREEPER,
            EntityType.SKELETON,
            EntityType.ENDERMAN,
            EntityType.IRON_GOLEM,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.TROPICAL_FISH,
            EntityType.PIGLIN,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.VILLAGER
    );

    private static double getSpawnCancelProbability() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        long activePlayers = Mirae.getState().getAccounts().stream()
                .filter(a -> {
                    LocalDateTime seen = a.getLastSeenTime();
                    return seen == null || seen.isAfter(cutoff);
                })
                .count();

        int onlinePlayers = Math.max(Bukkit.getOnlinePlayers().size() - Mirae.getState().getAfkAccounts().size(), 0);

        double onlineRate = (double) onlinePlayers / activePlayers;

        if (onlineRate >= 0.1) {
            return 0;
        }

        double base = 0.5;
        double rate = (0.1 - onlineRate) / 0.1;

        return base * rate;
    }

    @EventHandler
    public void onFarmableMobSpawn(EntitySpawnEvent e) {
        if (e.isCancelled()) return;
        if (!FARMABLE_MOBS.contains(e.getEntityType())) return;

        Random random = new Random();
        double rand = random.nextDouble();

        if (rand > getSpawnCancelProbability()) return;
        e.setCancelled(true);
    }
}
