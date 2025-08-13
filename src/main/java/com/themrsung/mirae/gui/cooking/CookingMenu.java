package com.themrsung.mirae.gui.cooking;

import com.themrsung.mirae.cooking.Cooking;
import com.themrsung.mirae.cooking.CookingRecipe;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * General cooking menu.
 */
public class CookingMenu extends AbstractCookingMenu {
    /**
     * Creates a new menu.
     *
     * @param player The player
     */
    public CookingMenu(@NotNull Player player) {
        super(player, Component.text("요리").style(Style.style()
                .color(TextColor.fromHexString("#ebcb2f"))
                .decorate(TextDecoration.BOLD)
                .build()));
    }

    @Override
    protected void renderConfirmButton() {
        ItemStack button;

        if (Cooking.getRecipes().stream().anyMatch(r -> r.cook(getIngredients()) != null)) {
            button = getConfirmButton();
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        } else {
            button = getCancelButton();
        }

        inventory.setItem(SLOT_CONFIRM, button);
    }

    @Override
    protected void onFirstSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onSecondSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onThirdSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onFourthSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onFifthSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onCookingConfirm() {
        Set<CookingRecipe> recipes = Cooking.getRecipes();
        for (CookingRecipe recipe : recipes) {
            ItemStack result = recipe.cook(getIngredients());

            if (result == null) continue;

            inventory.setItem(SLOT_FIRST, result);
            inventory.setItem(SLOT_SECOND, null);
            inventory.setItem(SLOT_THIRD, null);
            inventory.setItem(SLOT_FOURTH, null);
            inventory.setItem(SLOT_FIFTH, null);

            player.playSound(player, COOKING_SUCCESS_SOUND, 1, 1);

            return;
        }

        player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
    }
}
