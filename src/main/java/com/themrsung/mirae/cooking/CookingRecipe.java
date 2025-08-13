package com.themrsung.mirae.cooking;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Cooking recipe.
 */
@FunctionalInterface
public interface CookingRecipe {
    /**
     * Cooks the ingredients
     *
     * @param ingredients The ingredients (size = 5)
     * @return The cooked item
     */
    @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients);
}
