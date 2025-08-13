package com.themrsung.mirae.gui.cooking;

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

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract cooking menu.
 */
public abstract class AbstractCookingMenu extends AbstractGUI {
    public static final int MENU_SIZE = 27;

    public static final int SLOT_FIRST = 10;
    public static final int SLOT_SECOND = 11;
    public static final int SLOT_THIRD = 12;
    public static final int SLOT_FOURTH = 13;
    public static final int SLOT_FIFTH = 14;
    public static final int SLOT_CONFIRM = 16;

    public static final @NotNull Sound COOKING_SUCCESS_SOUND = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

    public static final @NotNull Component COOKING_SUCCESS_MESSAGE = Component.text("요리에 성공했습니다.").style(MX.STYLE_GOOD);

    public static final @NotNull Set<Integer> USER_SLOTS = Set.of(
            SLOT_FIRST,
            SLOT_SECOND,
            SLOT_THIRD,
            SLOT_FOURTH,
            SLOT_FIFTH
    );

    public static final @NotNull Set<Integer> ALL_SLOTS = Set.of(
            SLOT_FIRST,
            SLOT_SECOND,
            SLOT_THIRD,
            SLOT_FOURTH,
            SLOT_FIFTH,
            SLOT_CONFIRM
    );

    /**
     * Creates a new cooking menu
     *
     * @param player The player
     * @param title  The title
     */
    public AbstractCookingMenu(@NotNull Player player, @Nullable Component title) {
        super(player, MENU_SIZE, Objects.requireNonNullElse(title, Component.text("요리").style(MX.STYLE_GOOD)));

        this.account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        renderBackground();
        renderConfirmButton();
    }

    protected final @NotNull Account account;

    /**
     * Returns the first item.
     *
     * @return The first item
     */
    public @Nullable ItemStack getFirstItem() {
        return inventory.getItem(SLOT_FIRST);
    }

    /**
     * Returns the second item.
     *
     * @return The second item
     */
    public @Nullable ItemStack getSecondItem() {
        return inventory.getItem(SLOT_SECOND);
    }

    /**
     * Returns the third item.
     *
     * @return The third item
     */
    public @Nullable ItemStack getThirdItem() {
        return inventory.getItem(SLOT_THIRD);
    }

    /**
     * Returns the fourth item.
     *
     * @return The fourth item
     */
    public @Nullable ItemStack getFourthItem() {
        return inventory.getItem(SLOT_FOURTH);
    }

    /**
     * Returns the fifth item.
     *
     * @return The fifth item
     */
    public @Nullable ItemStack getFifthItem() {
        return inventory.getItem(SLOT_FIFTH);
    }

    /**
     * Returns the list of ingredients.
     *
     * @return The list of ingredients
     */
    public @NotNull List<ItemStack> getIngredients() {
        return USER_SLOTS.stream()
                .map(inventory::getItem)
                .filter(Objects::nonNull)
                .toList();
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
            case SLOT_FIRST -> onFirstSlotClick(e);
            case SLOT_SECOND -> onSecondSlotClick(e);
            case SLOT_THIRD -> onThirdSlotClick(e);
            case SLOT_FOURTH -> onFourthSlotClick(e);
            case SLOT_FIFTH -> onFifthSlotClick(e);
            case SLOT_CONFIRM -> onCookingConfirm();
        }

        renderBackground();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Mirae.getInstance(), this::renderConfirmButton, 1);
    }

    /**
     * Called to render background.
     */
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
     * Called to render the confirm button.
     */
    protected abstract void renderConfirmButton();

    /**
     * Called on first slot click.
     *
     * @param e The event
     */
    protected abstract void onFirstSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called on second slot click.
     *
     * @param e The event
     */
    protected abstract void onSecondSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called on third slot click.
     *
     * @param e The event
     */
    protected abstract void onThirdSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called on fourth slot click.
     *
     * @param e The event
     */
    protected abstract void onFourthSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called on fifth slot click.
     *
     * @param e The event
     */
    protected abstract void onFifthSlotClick(@NotNull InventoryClickEvent e);

    /**
     * Called on cooking confirm.
     */
    protected abstract void onCookingConfirm();

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        USER_SLOTS.forEach(slot -> {
            ItemStack item = inventory.getItem(slot);
            if (item == null) return;

            int remaining = MX.giveItems(player.getInventory(), item);
            if (remaining > 0) {
                ItemStack itemsToDrop = item.clone();
                itemsToDrop.setAmount(remaining);
                player.getWorld().dropItem(player.getLocation(), itemsToDrop);
            }
        });
    }
}
