package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Salt bread recipe.
 */
public class SaltBreadRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int bread = ingredients.stream()
                .filter(i -> i.getType() == Material.BREAD)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || bread <= 0) return null;
        if (bread != salt) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != bread + salt) return null;

        ItemStack saltBread = CustomItem.SALT_BREAD.getItem();
        saltBread.setAmount(bread);

        return saltBread;
    }
}
