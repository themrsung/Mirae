package com.themrsung.mirae.item.starwars;

import com.themrsung.mirae.item.ModifiableItemsAdderItem;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Red lightsaber.
 */
public class RedLightsaber extends ModifiableItemsAdderItem {
    /**
     * Creates a new lightsaber.
     */
    public RedLightsaber() {
        super("iaspecial_swords:red_lightsaber", "mirae.item.red_lightsaber");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<reset><red><bold>광선검<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build()));

        meta.setUnbreakable(true);

        item.setItemMeta(meta);
        return item;
    }
}
