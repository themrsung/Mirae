package com.themrsung.mirae.upgrade.enchant;

import com.themrsung.mirae.enchant.CustomEnchantment;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Custom enchantment acquisition recipe.
 */
public class CustomEnchantAcquisitionRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        boolean isEnchantedBook = left.getType() == Material.ENCHANTED_BOOK;
        boolean isBook = left.getType() == Material.BOOK;

        if (!isEnchantedBook && !isBook) return null;

        if (right.getType() != Material.NETHER_STAR) return null;
        if (right.getAmount() != 1) return null;

        Set<CustomEnchantment> existing = CustomEnchantment.getEnchantments(left).keySet();

        ItemStack result;

        if (isEnchantedBook) {
            result = left.clone();
        } else {
            result = ItemStack.of(Material.ENCHANTED_BOOK);
        }

        List<CustomEnchantment> possibleEnchantments = Arrays.stream(CustomEnchantment.Value.values())
                .filter(e -> !existing.contains(e))
                .map(v -> (CustomEnchantment) v)
                .toList();

        if (possibleEnchantments.isEmpty()) return null;

        List<CustomEnchantment> enchants = new ArrayList<>(possibleEnchantments);
        Collections.shuffle(enchants);

        enchants.getFirst().setEnchantLevel(result, 1); // Only level 1 for now
        return result;
    }

    @Override
    public double getSuccessRate() {
        return 1;
    }
}
