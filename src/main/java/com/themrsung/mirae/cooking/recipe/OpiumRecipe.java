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
public class OpiumRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int poppies = ingredients.stream()
                .filter(i -> i.getType() == Material.POPPY)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (poppies <= 0 || poppies % 16 != 0) {
            return null;
        }

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != poppies) return null;

        int opiumCount = poppies / 16;
        ItemStack opium = CustomItem.OPIUM.getItem();
        opium.setAmount(opiumCount);

        return opium;
    }
}
