package com.themrsung.mirae.item;

import com.themrsung.mirae.Mirae;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Modifiable custom item via ItemsAdder.
 */
public class ModifiableItemsAdderItem extends ItemsAdderItem implements Modifiable {
    /**
     * Creates a new item.
     *
     * @param instanceId The instance id
     * @param uniqueKey  The unique key
     */
    public ModifiableItemsAdderItem(@NotNull String instanceId, @NotNull String uniqueKey) {
        super(instanceId);

        this.uniqueKey = uniqueKey;
    }

    protected final @NotNull String uniqueKey;

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(new NamespacedKey(Mirae.getInstance(), uniqueKey), PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @NotNull String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public boolean isItem(@Nullable ItemStack item) {
        if (item == null) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        Boolean tag = data.get(new NamespacedKey(Mirae.getInstance(), uniqueKey), PersistentDataType.BOOLEAN);

        return tag != null && tag;
    }
}
