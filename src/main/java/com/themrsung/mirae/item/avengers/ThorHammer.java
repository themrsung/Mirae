package com.themrsung.mirae.item.avengers;

import com.themrsung.mirae.item.ModifiableItemsAdderItem;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Thor's hammer.
 */
public class ThorHammer extends ModifiableItemsAdderItem {
    /**
     * Creates a new Thor's Hammer.
     */
    public ThorHammer() {
        super("stellar_heroes:mythic_hammer", "mirae.item.thor_hammer");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><gradient:gold:blue><bold>묠니르<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build()));

        item.setItemMeta(meta);
        return item;
    }
}
