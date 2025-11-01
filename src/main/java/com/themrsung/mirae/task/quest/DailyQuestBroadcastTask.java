package com.themrsung.mirae.task.quest;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.state.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

/**
 * Periodically broadcasts the location of the active daily quest chest.
 */
public final class DailyQuestBroadcastTask implements Runnable {

    @Override
    public void run() {
        State state = Mirae.getState();
        Location location = state.getDailyQuestLocation();

        if (location == null) {
            return;
        }

        World world = location.getWorld();
        if (world == null) {
            return;
        }

        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;

        if (!world.isChunkLoaded(chunkX, chunkZ)) {
            return;
        }

        Block block = location.getBlock();
        Material type = block.getType();

        if (type != Material.CHEST && type != Material.TRAPPED_CHEST) {
            return;
        }

        if (!(block.getState() instanceof Chest chest)) {
            return;
        }

        Block above = block.getRelative(BlockFace.UP);
        if (above.getType().isOccluding()) {
            return;
        }

        Inventory inventory = chest.getBlockInventory();
        if (inventory.isEmpty()) {
            return;
        }

        DailyQuestTask.broadcastQuest(block.getLocation());
    }
}
