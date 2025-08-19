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
public class SausageRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int pork = ingredients.stream()
                .filter(i -> i.getType() == Material.PORKCHOP)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (salt <= 0 || pork <= 0) return null;
        if (pork * 2 != salt) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != pork + salt) return null;

        ItemStack sausage = CustomItem.SAUSAGE.getItem();
        sausage.setAmount(pork);

        return sausage;
    }
}
