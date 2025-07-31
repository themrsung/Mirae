package com.themrsung.mirae.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class CommandTypoListener implements Listener {
    private static final @NotNull Map<String, String> TYPO_MAP;

    static {
        Map<String, String> map = new HashMap<>();

        map.put("넴주", "spawn");
        map.put("ㄴㅔㅁㅈㅜ", "spawn");

        map.put("ㅠㅁ차", "back");
        map.put("ㅠㅁㅊㅏ", "back");

        map.put("ㄴㄷㅅ넴주", "setspawn");
        map.put("ㄴㄷㅅㄴㅔㅁㅈㅜ", "setspawn");

        map.put("ㅙㅡㄷ", "home");
        map.put("ㅗㅐㅡㄷ", "home");

        map.put("ㄴㄷ쇄ㅡㄷ", "sethome");
        map.put("ㄴㄷㅅㅗㅐㅡㄷ", "sethome");

        map.put("ㅇ디ㅙㅡㄷ", "delhome");
        map.put("ㅇㄷㅣㅗㅐㅡㄷ", "delhome");

        map.put("ㅙㅡㄷㄴ", "homes");
        map.put("ㅗㅐㅡㄷㄴ", "homes");

        TYPO_MAP = Map.copyOf(map);
    }

    public static @NotNull Map<String, String> getTypoMap() {
        return TYPO_MAP;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        String[] parts = message.split(" ");

        if (parts.length < 1) return;

        String label = parts[0].toLowerCase();
        if (TYPO_MAP.containsKey(label)) {
            e.setCancelled(true);

            parts[0] = TYPO_MAP.getOrDefault(label, label);
            String command = String.join(" ", parts);

            Bukkit.dispatchCommand(e.getPlayer(), command);
        }
    }
}
