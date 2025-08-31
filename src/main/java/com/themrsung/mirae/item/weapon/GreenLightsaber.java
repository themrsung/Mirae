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
 * Red lightsaber.
 */
public class GreenLightsaber extends ModifiableItemsAdderItem {
    /**
     * Creates a new lightsaber.
     */
    public GreenLightsaber() {
        super("iaspecial_swords:green_lightsaber", "mirae.item.green_lightsaber");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<reset><green><bold>광선검<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build());

        meta.itemName(name);
        meta.displayName(name);

        meta.setUnbreakable(true);

        item.setItemMeta(meta);
        return item;
    }
}
