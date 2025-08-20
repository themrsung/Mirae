package com.themrsung.mirae.upgrade.weapon;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Netherite sword to lightsaber.
 */
public class ClosedLightsaberRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) && ticket != null) return null;

        ItemStack netheriteAxe = new ItemStack(Material.NETHERITE_SWORD);
        if (!netheriteAxe.isSimilar(left)) return null;

        ItemStack uranium = CustomItem.URANIUM_INGOT.getItem();
        if (!uranium.isSimilar(right)) return null;
        if (right.getAmount() != 1) return null;

        ItemStack result = CustomItem.CLOSED_LIGHTSABER.getItem();

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
