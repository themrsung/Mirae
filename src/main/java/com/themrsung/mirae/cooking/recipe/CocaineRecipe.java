package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Opium Recipe.
 */
public class CocaineRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int paste = ingredients.stream()
                .filter(CustomItem.COCA_PASTE::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (paste <= 0 || salt <= 0) {
            return null;
        }

        if (paste * 3 != salt) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != paste + salt) return null;

        int cocaineCount = paste / 3;
        ItemStack cocaine = CustomItem.COCAINE.getItem();
        cocaine.setAmount(cocaineCount);

        return cocaine;
    }
}
