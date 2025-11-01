package com.themrsung.mirae.task.quest;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.item.EnchantedItemSupplier;
import com.themrsung.mirae.item.ItemSupplier;
import com.themrsung.mirae.item.lootbox.LootBox;
import com.themrsung.mirae.state.State;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Periodically generates a daily quest loot chest in the overworld.
 */
public final class DailyQuestTask implements Runnable {
    private static final ZoneId QUEST_ZONE = ZoneId.of("Asia/Seoul");
    private static final int COORDINATE_BOUND = 10_000;
    private static final int MAX_ATTEMPTS = 75;
    private static final @NotNull List<ItemSupplier> VALUABLE_ITEMS = List.of(
            CustomItem.THOR_HAMMER,
            CustomItem.RED_LIGHTSABER,
            CustomItem.GREEN_LIGHTSABER,
            CustomItem.MAGNET,
            CustomItem.VIBRANIUM_INGOT,
            CustomItem.URANIUM_INGOT,
            CustomItem.DONOR_COIN,
            EnchantedItemSupplier.ENCHANTED_NETHERITE_PICKAXE,
            EnchantedItemSupplier.ENCHANTED_NETHERITE_SWORD,
            EnchantedItemSupplier.ENCHANTED_NETHERITE_AXE,
            EnchantedItemSupplier.ENCHANTED_DIAMOND_SWORD,
            EnchantedItemSupplier.MENDING_BOOK,
            LootBox.ORANGE_BOX,
            LootBox.GREEN_BOX,
            LootBox.TITLE_BOX
    );

    @Override
    public void run() {
        State state = Mirae.getState();
        LocalDate today = LocalDate.now(QUEST_ZONE);

        Location storedLocation = state.getDailyQuestLocation();
        LocalDate storedDate = state.getDailyQuestDate();

        boolean needsQuest = storedLocation == null || storedDate == null || !storedDate.equals(today);

        if (!needsQuest && storedLocation != null) {
            Material type = storedLocation.getBlock().getType();
            if (type != Material.CHEST && type != Material.TRAPPED_CHEST) {
                needsQuest = true;
            }
        }

        if (!needsQuest) {
            return;
        }

        generateQuest(state, today);
    }

    private void generateQuest(@NotNull State state, @NotNull LocalDate today) {
        World world = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                .findFirst()
                .orElse(null);

        if (world == null) {
            Mirae.getInstance().getLogger().warning("Unable to locate an overworld world for the daily quest task.");
            return;
        }

        Location previous = state.getDailyQuestLocation();
        if (previous != null) {
            Material previousType = previous.getBlock().getType();
            if (previousType == Material.CHEST || previousType == Material.TRAPPED_CHEST) {
                previous.getBlock().setType(Material.AIR, false);
            }
        }

        Location questLocation = findQuestLocation(world);
        if (questLocation == null) {
            Mirae.getInstance().getLogger().warning("Failed to generate a location for today's daily quest chest.");
            state.setDailyQuestLocation(null);
            state.setDailyQuestDate(null);
            return;
        }

        Block chestBlock = questLocation.getBlock();
        chestBlock.setType(Material.CHEST, false);

        Chest chest = (Chest) chestBlock.getState();
        populateChest(chest.getInventory());
        chest.update(true);

        state.setDailyQuestLocation(chestBlock.getLocation());
        state.setDailyQuestDate(today);

        broadcastQuest(chestBlock.getLocation());
    }

    private @Nullable Location findQuestLocation(@NotNull World world) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int x = random.nextInt(-COORDINATE_BOUND, COORDINATE_BOUND + 1);
            int z = random.nextInt(-COORDINATE_BOUND, COORDINATE_BOUND + 1);

            world.getChunkAt(x >> 4, z >> 4).load();

            Block surface = world.getHighestBlockAt(x, z);
            if (!surface.getType().isSolid()) {
                continue;
            }

            Block chestBlock = surface.getRelative(BlockFace.UP);
            if (!chestBlock.isEmpty()) {
                continue;
            }

            Location location = chestBlock.getLocation();
            if (MX.getGriefPrevention().dataStore.getClaimAt(location, false, null) != null) {
                continue;
            }

            return location;
        }

        return null;
    }

    private void populateChest(@NotNull Inventory inventory) {
        inventory.clear();
        inventory.addItem(LootBox.PURPLE_BOX.getItem());
        inventory.addItem(LootBox.RED_BOX.getItem());

        List<ItemSupplier> pool = new ArrayList<>(VALUABLE_ITEMS);
        Collections.shuffle(pool);

        int additionalRewards = ThreadLocalRandom.current().nextInt(2, 5);
        for (int i = 0; i < additionalRewards && i < pool.size(); i++) {
            inventory.addItem(pool.get(i).getItem());
        }
    }

    private void broadcastQuest(@NotNull Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();

        Component message = Component.empty()
                .append(Component.text("[").style(MX.STYLE_NORMAL))
                .append(Component.text("일일 퀘스트").style(MX.STYLE_WARNING))
                .append(Component.text("] ").style(MX.STYLE_NORMAL))
                .append(Component.text("오버월드에 새로운 상자가 등장했습니다! ").style(MX.STYLE_SPECIAL))
                .append(Component.text("힌트: X ").style(MX.STYLE_NORMAL))
                .append(Component.text(Integer.toString(x)).style(MX.STYLE_GOOD))
                .append(Component.text(", Z ").style(MX.STYLE_NORMAL))
                .append(Component.text(Integer.toString(z)).style(MX.STYLE_GOOD));

        Bukkit.broadcast(message);
    }
}
