package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Ham recipe.
 */
public class AdvancedSteakRecipe implements CookingRecipe {
    private static final @NotNull ItemStack COOKED_BEEF = ItemStack.of(Material.COOKED_BEEF);

    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int steak = ingredients.stream()
                .filter(COOKED_BEEF::isSimilar)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || steak <= 0) return null;
        if (steak * 3 != salt) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != steak + salt) return null;

        ItemStack ham = CustomItem.ADVANCED_STEAK.getItem();
        ham.setAmount(steak);

        return ham;
    }
}
