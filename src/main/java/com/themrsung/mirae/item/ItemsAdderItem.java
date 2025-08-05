package com.themrsung.mirae.item;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom item via ItemsAdder.
 */
public class ItemsAdderItem implements CustomItem {
    /**
     * Creates a new ItemsAdder item.
     *
     * @param instanceId The instance ID
     */
    public ItemsAdderItem(@NotNull String instanceId) {
        this.instanceId = instanceId;
    }

    protected final @NotNull String instanceId;

    @Override
    public @NotNull ItemStack getItem() {
        CustomStack stack = CustomStack.getInstance(instanceId);
        if (stack == null) {
            throw new RuntimeException("Custom ItemsAdder item \"" + instanceId + "\" not found.");
        }

        return stack.getItemStack();
    }

    @Override
    public boolean isItem(@Nullable ItemStack item) {
        return getItem().isSimilar(item);
    }
}
