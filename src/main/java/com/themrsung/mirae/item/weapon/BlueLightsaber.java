package com.themrsung.mirae.item.weapon;

import com.themrsung.mirae.item.ModifiableItemsAdderItem;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Blue lightsaber.
 */
public class BlueLightsaber extends ModifiableItemsAdderItem {
    /**
     * Creates a new lightsaber.
     */
    public BlueLightsaber() {
        super("iaspecial_swords:blue_lightsaber", "mirae.item.blue_lightsaber");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><blue><bold>광선검<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build()));

        meta.setUnbreakable(true);

        item.setItemMeta(meta);
        return item;
    }
}
