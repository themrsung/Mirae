package com.themrsung.mirae.item.weapon;

import com.themrsung.mirae.item.ModifiableItemsAdderItem;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Captain America's Shield.
 */
public class CaptainShield extends ModifiableItemsAdderItem {
    /**
     * Creates a new shield.
     */
    public CaptainShield() {
        super("stellar_heroes:hero_shield", "mirae.item.captain_shield");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><gradient:blue:red><bold>비브라늄 방패<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build()));

        item.setItemMeta(meta);
        return item;
    }
}
