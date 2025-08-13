package com.themrsung.mirae.cooking.debug;

import com.themrsung.mirae.cooking.CookingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Test recipe.
 */
public class TestCookingRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int goldBlocks = ingredients.stream()
                .filter(i -> i.getType() == Material.GOLD_BLOCK)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int apples = ingredients.stream()
                .filter(i -> i.getType() == Material.APPLE)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (apples <= 0 || goldBlocks <= 0) return null;
        if (goldBlocks != apples * 4) return null;

        if (ingredients.stream()
                .anyMatch(i -> i.getType() != Material.GOLD_BLOCK && i.getType() != Material.APPLE)) return null;

        return ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE, apples);
    }
}
