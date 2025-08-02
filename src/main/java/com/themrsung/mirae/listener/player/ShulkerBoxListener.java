package com.themrsung.mirae.listener.player;

import com.themrsung.mirae.listener.Listeners;
import org.bukkit.Tag;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.UUID;

/**
 * Shulker box listener.
 */
public final class ShulkerBoxListener implements Listener {
    @EventHandler
    public void onInventoryClick(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        if (Listeners.GUI_ACTION_LISTENER.hasCloseCallback(player.getUniqueId())) return;
        if (player.isSneaking()) return;

        ItemStack item = e.getItem();

        if (item == null) return;
        if (!Tag.SHULKER_BOXES.isTagged(item.getType())) return;

        if (!(item.getItemMeta() instanceof BlockStateMeta bsm)) return;
        if (!(bsm.getBlockState() instanceof ShulkerBox box)) return;

        Inventory inventory = box.getInventory();

        if (Listeners.GUI_ACTION_LISTENER.registerCloseCallback(player.getUniqueId(), ignored -> {
            box.getInventory().setContents(inventory.getContents());
            bsm.setBlockState(box);
            item.setItemMeta(bsm);
        })) {
            e.setCancelled(true);
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!Listeners.GUI_ACTION_LISTENER.hasCloseCallback(uniqueId)) return;
        if (!Tag.SHULKER_BOXES.isTagged(e.getItemDrop().getItemStack().getType())) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onShulkerClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        UUID uniqueId = e.getWhoClicked().getUniqueId();
        if (!Listeners.GUI_ACTION_LISTENER.hasCloseCallback(uniqueId)) return;
        if (e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.PLAYER) return;

        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        if (!Tag.SHULKER_BOXES.isTagged(item.getType())) return;

        e.setCancelled(true);
    }
}
