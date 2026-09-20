package com.themrsung.mirae.item.weapon;

import com.themrsung.mirae.item.ModifiableItemsAdderItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Mythic hammer.
 */
public class MythicHammer extends ModifiableItemsAdderItem {
    /**
     * Creates a new Mythic hammer.
     */
    public MythicHammer() {
        super("stellar_heroes:mythic_hammer", "mirae.item.thor_hammer");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<reset><gradient:gold:blue><bold>신화의 망치<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build());

        meta.itemName(name);
        meta.displayName(name);

        item.setItemMeta(meta);
        return item;
    }
}
