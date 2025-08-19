package com.themrsung.mirae.cooking.recipe;

import com.themrsung.mirae.cooking.CookingRecipe;
import com.themrsung.mirae.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Army stew recipe.
 */
public class ArmyStewRecipe implements CookingRecipe {
    @Override
    public @Nullable ItemStack cook(@NotNull List<ItemStack> ingredients) {
        int water = ingredients.stream()
                .filter(i -> i.getType() == Material.WATER_BUCKET)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int salt = ingredients.stream()
                .filter(CustomItem.SALT::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int ham = ingredients.stream()
                .filter(CustomItem.HAM::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int sausage = ingredients.stream()
                .filter(CustomItem.SAUSAGE::isItem)
                .mapToInt(ItemStack::getAmount)
                .sum();

        int wheat = ingredients.stream()
                .filter(i -> i.getType() == Material.WHEAT)
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (water <= 0 || salt <= 0 || ham <= 0 || sausage <= 0 || wheat <= 0) return null;

        if (water * 16 != salt) return null;
        if (water * 16 != ham) return null;
        if (water * 16 != sausage) return null;
        if (water * 16 != wheat) return null;

        if (ingredients.stream().mapToInt(ItemStack::getAmount).sum() != water + salt + ham + sausage + wheat)
            return null;

        ItemStack stew = CustomItem.ARMY_STEW.getItem();
        stew.setAmount(water * 3);

        return stew;
    }
}
