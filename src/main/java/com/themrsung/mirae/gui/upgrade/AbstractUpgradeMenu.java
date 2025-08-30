package com.themrsung.mirae.gui.upgrade;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.gui.AbstractGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Abstract upgrade menu.
 */
public abstract class AbstractUpgradeMenu extends AbstractGUI {
    public static final int MENU_SIZE = 27;

    public static final int SLOT_LEFT = 10;
    public static final int SLOT_RIGHT = 12;
    public static final int SLOT_TICKET = 14;
    public static final int SLOT_CONFIRM = 16;

    public static final @NotNull Sound UPGRADE_SUCCESS_SOUND = Sound.UI_TOAST_CHALLENGE_COMPLETE;
    public static final @NotNull Sound UPGRADE_FAIL_SOUND = Sound.BLOCK_ANVIL_BREAK;

    public static final @NotNull Component UPGRADE_SUCCESS_MESSAGE = Component.text("강화에 성공했습니다!").style(MX.STYLE_GOOD);
    public static final @NotNull Component UPGRADE_FAIL_MESSAGE = Component.text("강화에 실패했습니다!").style(MX.STYLE_ERROR);

    /**
     * Slots which users can put items into.
     */
    public static final @NotNull Set<Integer> USER_SLOTS = Set.of(
            SLOT_LEFT,
            SLOT_RIGHT,
            SLOT_TICKET
    );

    /**
     * All UI slots.
     */
    public static final @NotNull Set<Integer> ALL_SLOTS = Set.of(
            SLOT_LEFT,
            SLOT_RIGHT,
            SLOT_TICKET,
            SLOT_CONFIRM
    );

    /**
     * Creates a new menu.
     *
     * @param player The player
     * @param title  The title
     */
    public AbstractUpgradeMenu(@NotNull Player player, @Nullable Component title) {
        super(player, MENU_SIZE, Objects.requireNonNullElse(title, Component.text("아이템 강화").style(MX.STYLE_GOOD)));

        this.account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        renderBackground();
        renderConfirmButton();
    }

    /**
     * The player account.
     */
    protected final @NotNull Account account;

    /**
     * Returns the left item.
     *
     * @return The left item if present, {@code null} if absent
     */
    public @Nullable ItemStack getLeftItem() {
        return inventory.getItem(SLOT_LEFT);
    }

    /**
     * Returns the right item.
     *
     * @return The right item if present, {@code null} if absent
     */
    public @Nullable ItemStack getRightItem() {
        return inventory.getItem(SLOT_RIGHT);
    }

    /**
     * Returns the ticket item.
     *
     * @return The ticket item if present, {@code null} if absent
     */
    public @Nullable ItemStack getTicketItem() {
        return inventory.getItem(SLOT_TICKET);
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        if (!Objects.equals(inventory, e.getClickedInventory())) return;
        if (!ALL_SLOTS.contains(e.getSlot())) {
            e.setCancelled(true);
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (e.getSlot() == SLOT_CONFIRM) {
            e.setCancelled(true);
        }

        switch (e.getSlot()) {
            case SLOT_LEFT -> onLeftSlotClick(e);
            case SLOT_RIGHT -> onRightSlotClick(e);
            case SLOT_TICKET -> onTicketSlotSlick(e);
            case SLOT_CONFIRM -> onUpgradeConfirm();
        }

        renderBackground();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Mirae.getInstance(), this::renderConfirmButton, 1);
    }

    protected void renderBackground() {
        ItemStack background = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = background.getItemMeta();

        meta.displayName(Component.empty());
        meta.itemName(Component.empty());
        background.setItemMeta(meta);

        for (int i = 0; i < MENU_SIZE; i++) {
            if (ALL_SLOTS.contains(i)) continue;
            inventory.setItem(i, background);
        }
    }

    /**
     * Called when the confirm button should be re-rendered.
     */
    protected abstract void renderConfirmButton();

    /**
     * Called when the left item slot is clicked.
     *
     * @param e The event
     */
    protected abstract void onLeftSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called when the right item slot is clicked.
     *
     * @param e The event
     */
    protected abstract void onRightSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called when the ticket item slot is clicked.
     *
     * @param e The event
     */
    protected abstract void onTicketSlotSlick(@NotNull InventoryClickEvent e);

    /**
     * Called when the upgrade confirm button is clicked.
     */
    protected abstract void onUpgradeConfirm();

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        USER_SLOTS.forEach(slot -> {
            ItemStack item = inventory.getItem(slot);
            if (item == null) return;

            MX.giveItems(player, item);
        });
    }
}
