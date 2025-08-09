package com.themrsung.mirae.item.economy;

import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.account.AccountTitleQueryResult;
import com.themrsung.mirae.item.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Account title item.
 */
public class TitleItem implements CustomItem {
    /**
     * The lore.
     */
    public static final @NotNull Component LORE = Component.text("우클릭하여 계정에 추가합니다.")
            .style(Style.style()
                    .color(TextColor.fromHexString("#cccccc"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build());

    /**
     * Creates a new title item.
     *
     * @param title The title
     */
    public TitleItem(@NotNull AccountTitle title) {
        this.title = title;
    }

    private final @NotNull AccountTitle title;

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = stack.getItemMeta();

        meta.itemName(Component.text(title.getKey()));
        meta.displayName(title.getValue());
        meta.lore(List.of(LORE));

        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public boolean isItem(@Nullable ItemStack item) {
        AccountTitleQueryResult result = AccountTitle.isTitle(item);
        return result.result() && Objects.equals(result.title(), title);
    }
}
