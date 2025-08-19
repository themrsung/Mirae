package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Apple pie recipe.
 */
public class ApplePieRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int apple = ingredients.stream()
                .filter(i -> i.getType() == Material.APPLE)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int sugar = ingredients.stream()
                .filter(i -> i.getType() == Material.SUGAR)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (sugar <= 0 || apple <= 0) return null;
        if (apple * 4 != sugar) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != apple + sugar) return null;

        ItemStack applePie = CustomItem.APPLE_PIE.getItem();
        applePie.setAmount(apple);

        return applePie;
    }
}
