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
public class CocaPasteRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int leaves = ingredients.stream()
                .filter(i -> i.getType() == Material.JUNGLE_LEAVES)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (leaves <= 0 || leaves % 16 != 0) {
            return null;
        }

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != leaves) return null;

        int pasteCount = leaves / 16;
        ItemStack paste = CustomItem.COCA_PASTE.getItem();
        paste.setAmount(pasteCount);

        return paste;
    }
}
