package com.themrsung.mirae.item.avengers;

import com.themrsung.mirae.item.ItemsAdderItem;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Thor's axe (a.k.a. Stormbreaker)
 */
public class Stormbreaker extends ItemsAdderItem {
    /**
     * Creates a new Thor's axe;
     */
    public Stormbreaker() {
        super("stellar_heroes:storm_hammer");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><gradient:gold:black><bold>스톰브레이커<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build()));

        item.setItemMeta(meta);
        return item;
    }
}
