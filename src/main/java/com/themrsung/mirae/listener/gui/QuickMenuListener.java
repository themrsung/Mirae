package com.themrsung.mirae.listener.gui;

import com.themrsung.mirae.gui.GUI;
import com.themrsung.mirae.gui.MainMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * Listens for Shift+F.
 */
public class QuickMenuListener implements Listener {
    @EventHandler
    public void onShiftSwap(PlayerSwapHandItemsEvent e) {
        if (e.isCancelled() || !e.getPlayer().isSneaking()) return;

        GUI mainMenu = new MainMenu(e.getPlayer());
        mainMenu.openGUI();

        e.setCancelled(true);
    }
}
