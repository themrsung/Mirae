package com.themrsung.mirae.upgrade.debug;

import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Test recipe.
 */
public class TestRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        if (!diamondSword.isSimilar(left) || !diamondSword.isSimilar(right)) return null;

        return new ItemStack(Material.NETHERITE_SWORD);
    }

    @Override
    public double getSuccessRate() {
        return 0.5;
    }
}
