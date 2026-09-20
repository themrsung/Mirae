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
 * Hero shield.
 */
public class HeroShield extends ModifiableItemsAdderItem {
    /**
     * Creates a new shield.
     */
    public HeroShield() {
        super("stellar_heroes:hero_shield", "mirae.item.captain_shield");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<reset><gradient:blue:red><bold>영웅의 방패<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build());

        meta.itemName(name);
        meta.displayName(name);

        item.setItemMeta(meta);
        return item;
    }
}
