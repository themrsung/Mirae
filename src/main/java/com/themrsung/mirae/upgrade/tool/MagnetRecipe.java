package com.themrsung.mirae.upgrade.tool;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Magnet recipe.
 */
public class MagnetRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.DARKSTEEL_INGOT.isItem(left)) return null;
        if (!CustomItem.URANIUM_RAW.isItem(right)) return null;

        int amount = left.getAmount();
        if (amount != 1 || amount != right.getAmount()) return null;

        return CustomItem.MAGNET.getItem();
    }

    @Override
    public double getSuccessRate() {
        return 1;
    }
}
