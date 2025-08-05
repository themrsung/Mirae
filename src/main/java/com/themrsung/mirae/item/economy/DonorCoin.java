package com.themrsung.mirae.item.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.item.ItemsAdderItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DonorCoin extends ItemsAdderItem {
    /**
     * Creates a new Thor's Hammer.
     */
    public DonorCoin() {
        super("iageneric:coin");
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack item = super.getItem();
        ItemMeta meta = item.getItemMeta();

        Component name = MiniMessage.miniMessage().deserialize("<gradient:green:gold>후원 코인<reset>")
                .style(Style.style().decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).build());

        meta.itemName(name);
        meta.displayName(name);
        meta.lore(List.of(
                Component.text("후원을 통해 얻을 수 있습니다.").style(MX.STYLE_NORMAL)
        ));

        item.setItemMeta(meta);

        return item;
    }
}
