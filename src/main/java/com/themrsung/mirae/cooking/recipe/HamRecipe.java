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
public class HamRecipe implements CookingRecipe {
    private static final @NotNull ItemStack COOKED_PORK = ItemStack.of(Material.COOKED_PORKCHOP);

    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int pork = ingredients.stream()
                .filter(COOKED_PORK::isSimilar)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || pork <= 0) return null;
        if (pork * 3 != salt) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != pork + salt) return null;

        ItemStack ham = CustomItem.HAM.getItem();
        ham.setAmount(pork);

        return ham;
    }
}
