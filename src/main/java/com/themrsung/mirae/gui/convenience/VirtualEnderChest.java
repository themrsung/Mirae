package com.themrsung.mirae.gui.convenience;

import com.themrsung.mirae.gui.AbstractGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * Virtual ender chest.
 */
public class VirtualEnderChest extends AbstractGUI {
    /**
     * Creates a new ender chest.
     *
     * @param player The player
     */
    public VirtualEnderChest(@NotNull Player player) {
        super(player, Bukkit.createInventory(player, InventoryType.ENDER_CHEST));
        inventory.setContents(player.getEnderChest().getContents());
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        player.getEnderChest().setContents(inventory.getContents());
    }
}
