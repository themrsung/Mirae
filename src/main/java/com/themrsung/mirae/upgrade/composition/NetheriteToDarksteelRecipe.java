package com.themrsung.mirae.upgrade.composition;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Netherite to darksteel.
 */
public class NetheriteToDarksteelRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        ItemStack netheriteIngot = new ItemStack(Material.NETHERITE_INGOT);
        if (!netheriteIngot.isSimilar(left)) return null;

        ItemStack uraniumIngot = CustomItem.URANIUM_INGOT.getItem();
        if (!uraniumIngot.isSimilar(right)) return null;

        int amount = left.getAmount();
        if (amount != right.getAmount()) return null;

        ItemStack darksteelIngot = CustomItem.DARKSTEEL_INGOT.getItem();
        darksteelIngot.setAmount(amount);

        return darksteelIngot;
    }

    @Override
    public double getSuccessRate() {
        return 0.95;
    }
}
