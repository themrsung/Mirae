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
 * Beam sword handle.
 */
public class ClosedBeamSword extends ModifiableItemsAdderItem {
    /**
     * Creates a new beam sword.
     */
    public ClosedBeamSword() {
        super("iaspecial_swords:closed_lightsaber", "mirae.item.closed_lightsaber");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<reset><color:#2975e6><bold>광선검 손잡이<reset>")
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
