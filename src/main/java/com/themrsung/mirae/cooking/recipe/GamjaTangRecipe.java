package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Gamja tang recipe.
 */
public class GamjaTangRecipe implements CookingRecipe {
    private static final @NotNull ItemStack BONE_BLOCK = ItemStack.of(Material.BONE_BLOCK);
    private static final @NotNull ItemStack POTATO = ItemStack.of(Material.POTATO);

    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int boneBlocks = ingredients.stream()
                .filter(BONE_BLOCK::isSimilar)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int potatoes = ingredients.stream()
                .filter(POTATO::isSimilar)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (boneBlocks <= 0 || potatoes <= 0 || salt <= 0) return null;
        if (boneBlocks != potatoes || boneBlocks != salt) return null;

        int totalItems = ingredients.stream()
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (totalItems != boneBlocks + potatoes + salt) return null;

        ItemStack gamjaTang = CustomItem.GAMJA_TANG.getItem();
        gamjaTang.setAmount(boneBlocks);
        return gamjaTang;
    }
}
