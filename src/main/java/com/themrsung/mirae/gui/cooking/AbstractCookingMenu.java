package com.themrsung.mirae.gui.cooking;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.gui.AbstractGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {

    }

    protected void renderBackground() {
        // TODO 
    }

    protected abstract void renderConfirmButton();

    protected abstract void onFirstSlotClick(@NotNull InventoryClickEvent e);

    protected abstract void onSecondSlotClick(@NotNull InventoryClickEvent e);

    protected abstract void onThirdSlotClick(@NotNull InventoryClickEvent e);

    protected abstract void onFourthSlotClick(@NotNull InventoryClickEvent e);

    protected abstract void onFifthSlotClick(@NotNull InventoryClickEvent e);

    protected abstract void onCookingConfirm();

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {

    }
}
