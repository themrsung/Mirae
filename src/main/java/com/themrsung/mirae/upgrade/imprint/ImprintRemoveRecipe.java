package com.themrsung.mirae.upgrade.imprint;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Removes imprint.
 */
public class ImprintRemoveRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        ItemMeta meta = left.getItemMeta();
        List<Component> lore = meta.lore();

        if (lore == null || lore.stream().noneMatch(l -> Objects.equals(l, SUCCESS_TICKET_IMPRINT))) return null;

        if (!CustomItem.DONOR_COIN.isItem(right) || right.getAmount() != 3) return null;
        if (right.getAmount() != 3) return null;

        lore.removeIf(l -> Objects.equals(l, SUCCESS_TICKET_IMPRINT));
        meta.lore(lore);

        ItemStack result = left.clone();
        ItemMeta resultMeta = result.getItemMeta();

        resultMeta.lore(lore);

        result.setItemMeta(resultMeta);
        return result;
    }
}
