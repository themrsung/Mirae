package com.themrsung.mirae.upgrade.composition;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Coal to diamonds.
 */
public class CoalToDiamondRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) && ticket != null) return null;

        ItemStack coal = new ItemStack(Material.COAL);
        if (!coal.isSimilar(left)) return null;

        ItemStack uraniumIngot = CustomItem.URANIUM_RAW.getItem();
        if (!uraniumIngot.isSimilar(right)) return null;

        int amount = left.getAmount();
        if (amount != right.getAmount()) return null;

        ItemStack diamond = new ItemStack(Material.DIAMOND);
        diamond.setAmount(amount);

        return diamond;
    }

    @Override
    public double getSuccessRate() {
        return 1;
    }
}
