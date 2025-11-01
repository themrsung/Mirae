package com.themrsung.mirae.listener.quest;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.state.State;
import com.themrsung.mirae.task.quest.DailyQuestTask;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Populates the daily quest chest when it is opened.
 */
public final class DailyQuestChestListener implements Listener {
    private static final ZoneId QUEST_ZONE = ZoneId.of("Asia/Seoul");

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        Location inventoryLocation = inventory.getLocation();
        if (inventoryLocation == null) {
            return;
        }

        State state = Mirae.getState();
        Location questLocation = state.getDailyQuestLocation();
        if (questLocation == null || questLocation.getWorld() == null) {
            return;
        }

        if (inventoryLocation.getWorld() == null || !isSameBlock(questLocation, inventoryLocation)) {
            return;
        }

        if (state.isDailyQuestRewardGenerated()) {
            return;
        }

        LocalDateTime generatedAt = state.getDailyQuestGeneratedAt();
        if (generatedAt == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now(QUEST_ZONE);
        LocalDateTime expiration = generatedAt.plusHours(24);
        if (!now.isBefore(expiration)) {
            state.setDailyQuestRewardGenerated(true);
            return;
        }

        DailyQuestTask.populateChest(inventory);
        state.setDailyQuestRewardGenerated(true);
    }

    private boolean isSameBlock(@NotNull Location first, @NotNull Location second) {
        return first.getWorld().equals(second.getWorld())
                && first.getBlockX() == second.getBlockX()
                && first.getBlockY() == second.getBlockY()
                && first.getBlockZ() == second.getBlockZ();
    }
}
