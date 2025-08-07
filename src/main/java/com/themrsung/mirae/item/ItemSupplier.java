package com.themrsung.mirae.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Item provider.
 */
@FunctionalInterface
public interface ItemSupplier {
    /**
     * Returns a new item instance.
     *
     * @return The item
     */
    @NotNull ItemStack getItem();
}
