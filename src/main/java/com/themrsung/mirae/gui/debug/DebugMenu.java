package com.themrsung.mirae.gui.debug;

import com.themrsung.mirae.gui.AbstractGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * The debug menu.
 */
public class DebugMenu extends AbstractGUI {
    /**
     * Creates a new menu.
     *
     * @param player The player
     */
    public DebugMenu(@NotNull Player player) {
        super(player, 27, Component.text("debug"));

        inventory.setItem(0, getPreviousButton());
        inventory.setItem(1, getNextButton());
        inventory.setItem(2, getBackButton());
        inventory.setItem(3, getCancelButton());
        inventory.setItem(4, getConfirmButton());
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);

        Inventory clicked = e.getClickedInventory();
        if (clicked == null || clicked.getHolder() != null) return;

        switch (e.getSlot()) {
            case 0 -> player.sendMessage("이전");
            case 1 -> player.sendMessage("다음");
            case 2 -> player.sendMessage("뒤로");
            case 3 -> player.sendMessage("취소");
            case 4 -> player.sendMessage("확정");
        }
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {

    }
}
