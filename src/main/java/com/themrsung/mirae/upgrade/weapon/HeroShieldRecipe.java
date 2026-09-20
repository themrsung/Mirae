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
 * Captain's shield recipe.
 */
public class HeroShieldRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) && ticket != null) return null;

        ItemStack shield = new ItemStack(Material.SHIELD);
        if (!shield.isSimilar(left)) return null;

        ItemStack darksteel = CustomItem.DARKSTEEL_INGOT.getItem();
        if (!darksteel.isSimilar(right)) return null;
        if (right.getAmount() != 9) return null;

        ItemStack result = CustomItem.HERO_SHIELD.getItem();

        if (CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket)) {
            ItemMeta meta = result.getItemMeta();
            meta.lore(List.of(SUCCESS_TICKET_IMPRINT));
            result.setItemMeta(meta);
        }

        return result;
    }

    @Override
    public double getSuccessRate() {
        return 0.005;
    }
}
