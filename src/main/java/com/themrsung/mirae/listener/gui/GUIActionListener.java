package com.themrsung.mirae.listener.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Listens for and handles GUI actions.
 */
public class GUIActionListener implements Listener {
    /**
     * Creates a new listener.
     */
    public GUIActionListener() {

        this.onCloseCallbacks = new ConcurrentHashMap<>();
        this.onClickCallbacks = new ConcurrentHashMap<>();
    }

    private final @NotNull Map<UUID, Consumer<InventoryCloseEvent>> onCloseCallbacks;
    private final @NotNull Map<UUID, Consumer<InventoryClickEvent>> onClickCallbacks;

    /**
     * Returns whether there is a callback with the given key.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if there is a callback
     */
    public boolean hasCallback(@NotNull UUID uniqueId) {
        return onClickCallbacks.containsKey(uniqueId) || onCloseCallbacks.containsKey(uniqueId);
    }

    /**
     * Registers an on close callback.
     *
     * @param uniqueId The unique identifier of the player
     * @param callback The callback
     * @return {@code true} if the callback was registered
     */
    public boolean registerCloseCallback(@NotNull UUID uniqueId, @NotNull Consumer<InventoryCloseEvent> callback) {
        Objects.requireNonNull(uniqueId);
        Objects.requireNonNull(callback);

        if (onCloseCallbacks.containsKey(uniqueId)) return false;

        onCloseCallbacks.put(uniqueId, callback);
        return true;
    }

    /**
     * Returns whether the provided unique identifier has a callback.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if the unique identifier has a callback
     */
    public boolean hasCloseCallback(@Nullable UUID uniqueId) {
        return onCloseCallbacks.containsKey(uniqueId);
    }

    /**
     * Removes the close callback.
     *
     * @param uniqueId The unique identifier
     * @return The callback which was removed, or {@code null}
     */
    public @Nullable Consumer<InventoryCloseEvent> removeCloseCallback(@Nullable UUID uniqueId) {
        return onCloseCallbacks.remove(uniqueId);
    }

    /**
     * Registers an on click callback.
     *
     * @param uniqueId The unique identifier of the player
     * @param callback The callback
     * @return {@code true} if the callback was registered
     */
    public boolean registerClickCallback(@NotNull UUID uniqueId, @NotNull Consumer<InventoryClickEvent> callback) {
        Objects.requireNonNull(uniqueId);
        Objects.requireNonNull(callback);

        if (onClickCallbacks.containsKey(uniqueId)) return false;

        onClickCallbacks.put(uniqueId, callback);
        return true;
    }

    /**
     * Returns whether the provided unique identifier has a callback.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if the unique identifier has a callback
     */
    public boolean hasClickCallback(@Nullable UUID uniqueId) {
        return onClickCallbacks.containsKey(uniqueId);
    }

    /**
     * Removes the click callback.
     *
     * @param uniqueId The unique identifier
     * @return The callback which was removed, or {@code null}
     */
    public @Nullable Consumer<InventoryClickEvent> removeClickCallbacks(@Nullable UUID uniqueId) {
        return onClickCallbacks.remove(uniqueId);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Consumer<InventoryCloseEvent> callback = onCloseCallbacks.remove(e.getPlayer().getUniqueId());
        if (callback == null) return;

        try {
            callback.accept(e);
            onClickCallbacks.remove(e.getPlayer().getUniqueId());
        } catch (Exception ex) {
            throw new RuntimeException("Error executing inventory close callback for " + e.getPlayer().getUniqueId(), ex);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        Consumer<InventoryClickEvent> callback = onClickCallbacks.get(e.getWhoClicked().getUniqueId());
        if (callback == null) return;

        try {
            callback.accept(e);
        } catch (Exception ex) {
            throw new RuntimeException("Error executing inventory close callback for " + e.getWhoClicked().getUniqueId(), ex);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwapHands(PlayerSwapHandItemsEvent e) {
        if (onClickCallbacks.containsKey(e.getPlayer().getUniqueId())) {
            InventoryView view = e.getPlayer().getOpenInventory();
            if (view.getType() == InventoryType.ENDER_CHEST || view.getType() == InventoryType.WORKBENCH) return;

            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSendItemToOtherInventory(InventoryClickEvent e) {
        if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) return;

        UUID uniqueId = e.getWhoClicked().getUniqueId();
        if (!onClickCallbacks.containsKey(uniqueId)) return;

        InventoryView view = e.getWhoClicked().getOpenInventory();
        if (view.getType() == InventoryType.ENDER_CHEST || view.getType() == InventoryType.WORKBENCH) return;

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreativeClick(InventoryCreativeEvent e) {
        if (onClickCallbacks.containsKey(e.getWhoClicked().getUniqueId())) {
            InventoryView view = e.getWhoClicked().getOpenInventory();
            if (view.getType() == InventoryType.ENDER_CHEST || view.getType() == InventoryType.WORKBENCH) return;

            e.setCancelled(true);
        }
    }
}
