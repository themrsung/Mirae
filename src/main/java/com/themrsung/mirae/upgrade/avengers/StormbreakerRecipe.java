package com.themrsung.mirae.upgrade.avengers;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Stormbreaker recipe.
 */
public class StormbreakerRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) && ticket != null) return null;

        ItemStack netheriteAxe = new ItemStack(Material.NETHERITE_AXE);
        if (!netheriteAxe.isSimilar(left)) return null;

        ItemStack vibranium = CustomItem.VIBRANIUM_INGOT.getItem();
        if (!vibranium.isSimilar(right)) return null;
        if (right.getAmount() != 1) return null;

        ItemStack result = CustomItem.STORMBREAKER.getItem();

        if (CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket)) {
            ItemMeta meta = result.getItemMeta();
            meta.lore(List.of(SUCCESS_TICKET_IMPRINT));
            result.setItemMeta(meta);
        }

        return result;
    }

    @Override
    public double getSuccessRate() {
        return 0.01;
    }
}
