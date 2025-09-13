package com.themrsung.mirae.listener.server;

import com.themrsung.mirae.MX;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Phantom removal.
 */
public class PhantomCancellationListener implements Listener {
    @EventHandler
    public void onPhantomSpawned(EntitySpawnEvent e) {
        if (e.getEntityType() != EntityType.PHANTOM) return;

        Location location = e.getLocation();
        Claim claim = MX.getGriefPrevention().dataStore
                .getClaimAt(location, true, null);

        if (claim != null) e.setCancelled(true);
    }
}
