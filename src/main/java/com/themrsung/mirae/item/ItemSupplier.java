package com.themrsung.mirae.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Item provider.
 */
@FunctionalInterface
public interface ItemSupplier {
    /**
     * Starter kit.
     */
    @NotNull StarterKit STARTER_KIT = new StarterKit();

    /**
     * Returns a new item instance.
     *
     * @return The item
     */
    @NotNull ItemStack getItem();
}
