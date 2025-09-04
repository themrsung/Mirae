package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Opium Recipe.
 */
public class MorphineRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int opium = ingredients.stream()
                .filter(CustomItem.OPIUM::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int bottle = ingredients.stream()
                .filter(i -> i.getType() == Material.GLASS_BOTTLE)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (opium <= 0 || bottle <= 0) {
            return null;
        }

        if (opium * 3 != bottle) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != opium + bottle) return null;

        int morphineCount = opium / 3;
        ItemStack morphine = CustomItem.MORPHINE.getItem();
        morphine.setAmount(morphineCount);

        return morphine;
    }
}
