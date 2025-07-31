package com.themrsung.mirae.listener.social;

import com.themrsung.mirae.event.social.DirectMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class SocialSpyListener implements Listener {
    @EventHandler
    public void onDirectMessageSent(DirectMessageSentEvent e) {
        Bukkit.getConsoleSender().sendMessage(e.getMessage().asShownToConsole());

        Bukkit.getOperators().forEach(op -> {
            Player player = op.getPlayer();
            if (player == null || !player.isOnline()) return;

            if (Objects.equals(player.getUniqueId(), e.getMessage().sender().getUniqueId())) return;
            if (Objects.equals(player.getUniqueId(), e.getMessage().recipient().getUniqueId())) return;

            player.sendMessage(e.getMessage().asShownToThirdParty());
        });
    }
}
