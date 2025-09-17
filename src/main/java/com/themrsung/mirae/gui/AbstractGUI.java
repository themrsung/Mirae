package com.themrsung.mirae.gui;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.listener.gui.GUIActionListener;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Abstract GUI class.
 */
public abstract class AbstractGUI implements GUI {
    /// Button IDS

    protected static final @NotNull String PREVIOUS_BUTTON_ID = "mcicons:icon_left_gray";
    protected static final @NotNull String NEXT_BUTTON_ID = "mcicons:icon_right_gray";
    protected static final @NotNull String BACK_BUTTON_ID = "mcicons:icon_back_orange";

    protected static final @NotNull String CANCEL_BUTTON_ID = "mcicons:icon_cancel";
    protected static final @NotNull String CONFIRM_BUTTON_ID = "mcicons:icon_confirm";

    /// Button getters

    /**
     * Returns a new previous button.
     *
     * @return A new previous button
     */
    protected static @NotNull ItemStack getPreviousButton() {
        return getButton(
                PREVIOUS_BUTTON_ID,
                Component.text("이전").style(MX.STYLE_NORMAL),
                List.of(Component.text("이전으로 이동합니다.").style(MX.STYLE_NORMAL))
        );
    }

    /**
     * Returns a new next button.
     *
     * @return A new next button
     */
    protected static @NotNull ItemStack getNextButton() {
        return getButton(
                NEXT_BUTTON_ID,
                Component.text("다음").style(MX.STYLE_NORMAL),
                List.of(Component.text("다음으로 이동합니다.").style(MX.STYLE_NORMAL))
        );
    }

    /**
     * Returns a new back button.
     *
     * @return A new back button
     */
    protected static @NotNull ItemStack getBackButton() {
        return getButton(
                BACK_BUTTON_ID,
                Component.text("뒤로").style(MX.STYLE_NORMAL),
                List.of(Component.text("뒤로 이동합니다.").style(MX.STYLE_NORMAL))
        );
    }

    /**
     * Returns a new cancel button.
     *
     * @return A new cancel button
     */
    protected static @NotNull ItemStack getCancelButton() {
        return getButton(
                CANCEL_BUTTON_ID,
                Component.text("취소").style(MX.STYLE_WARNING),
                List.of()
        );
    }

    /**
     * Returns a new confirm button.
     *
     * @return A new confirm button
     */
    protected static @NotNull ItemStack getConfirmButton() {
        return getButton(
                CONFIRM_BUTTON_ID,
                Component.text("확인").style(MX.STYLE_GOOD),
                List.of()
        );
    }

    /**
     * Helper method to get ItemStack buttons.
     *
     * @param id          The namespace ID
     * @param displayName The display name
     * @param lore        The lore
     * @return The item
     */
    private static @NotNull ItemStack getButton(@NotNull String id, @NotNull Component displayName, @NotNull List<Component> lore) {
        CustomStack stack = CustomStack.getInstance(id);
        if (stack == null) return new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemStack item = stack.getItemStack();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(displayName);
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a new GUI instance.
     *
     * @param player The player using the GUI
     * @param size   The size of the inventory
     * @param title  The title of the inventory
     */
    protected AbstractGUI(@NotNull Player player, int size, @Nullable Component title) {
        this(player, Bukkit.createInventory(null, size, title != null ? title : Component.empty()));
    }

    /**
     * Creates a new GUI instance.
     *
     * @param player    The player using the GUI
     * @param inventory The inventory
     */
    protected AbstractGUI(@NotNull Player player, @NotNull Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    protected final @NotNull Player player;
    protected final @NotNull Inventory inventory;

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public final void openGUI() {
        UUID uniqueId = player.getUniqueId();

        GUIActionListener listener = Listeners.GUI_ACTION_LISTENER;
        if (listener.hasCallback(uniqueId)) {
            // Close existing menu
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }

        player.openInventory(inventory);

        listener.registerOpenMenu(uniqueId, this);
        listener.registerClickCallback(uniqueId, this::onClick);
        listener.registerCloseCallback(uniqueId, this::onClose);
    }

    /**
     * Called upon inventory click.
     *
     * @param e The click event
     */
    protected abstract void onClick(@NotNull InventoryClickEvent e);

    /**
     * Called upon inventory close.
     *
     * @param e The close event
     */
    protected abstract void onClose(@NotNull InventoryCloseEvent e);
}
