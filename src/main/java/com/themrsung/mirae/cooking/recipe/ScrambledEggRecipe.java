package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Scrambled egg recipe.
 */
public class ScrambledEggRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int egg = ingredients.stream()
                .filter(i -> i.getType() == Material.EGG)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || egg <= 0) return null;
        if (egg != salt * 3) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != egg + salt) return null;

        ItemStack scrambledEgg = CustomItem.SCRAMBLED_EGG.getItem();
        scrambledEgg.setAmount(salt);

        return scrambledEgg;
    }
}
