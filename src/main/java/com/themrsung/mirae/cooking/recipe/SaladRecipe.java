package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Sausage recipe.
 */
public class SaladRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int grass = ingredients.stream()
                .filter(i -> i.getType() == Material.SHORT_GRASS)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || grass <= 0) return null;
        if (grass != salt * 3) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != grass + salt) return null;

        ItemStack salad = CustomItem.SALAD.getItem();
        salad.setAmount(salt);

        return salad;
    }
}
