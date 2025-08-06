package com.themrsung.mirae.listener.abuse;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Anti spam listener.
 */
public class AntiSpamListener implements Listener {
    /**
     * Creates a new listener.
     */
    public AntiSpamListener() {
        this.chatCountMap = new HashMap<>();
        this.commandCountMap = new HashMap<>();
    }

    private final @NotNull Map<UUID, Integer> chatCountMap;
    private final @NotNull Map<UUID, Integer> commandCountMap;

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        UUID playerId = e.getPlayer().getUniqueId();
        int count = chatCountMap.getOrDefault(playerId, 0);
        chatCountMap.put(playerId, count + 1);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        UUID playerId = e.getPlayer().getUniqueId();
        int count = commandCountMap.getOrDefault(playerId, 0);
        commandCountMap.put(playerId, count + 1);
    }

    /**
     * Clears the spam count maps.
     */
    public void clearSpamCounts() {
        chatCountMap.clear();
        commandCountMap.clear();
    }

    /**
     * Executes a for-each loop.
     *
     * @param consumer The consumer
     */
    public void forEachChat(@NotNull BiConsumer<UUID, Integer> consumer) {
        chatCountMap.forEach(consumer);
    }

    /**
     * Executes a for-each loop.
     *
     * @param consumer The consumer
     */
    public void forEachCommand(@NotNull BiConsumer<UUID, Integer> consumer) {
        commandCountMap.forEach(consumer);
    }
}
