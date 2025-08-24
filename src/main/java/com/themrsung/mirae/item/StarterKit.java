package com.themrsung.mirae.item;

import com.themrsung.mirae.MX;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Starter kit.
 */
public class StarterKit implements ItemSupplier {
    @Override
    public @NotNull ItemStack getItem() {
        ItemStack box = ItemStack.of(Material.BLACK_SHULKER_BOX);
        BlockStateMeta bsm = (BlockStateMeta) box.getItemMeta();
        ShulkerBox state = (ShulkerBox) bsm.getBlockState();

        ItemStack ham = CustomItem.HAM.getItem();
        ham.setAmount(32);

        state.getInventory().setContents(new ItemStack[] {
                ItemStack.of(Material.IRON_AXE),
                ItemStack.of(Material.GOLDEN_SHOVEL),
                ham
        });

        bsm.displayName(Component.text("스타터킷").style(Style.style()
                .color(TextColor.fromHexString("#ffd23c"))
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
                .build()));

        bsm.lore(List.of(
                Component.text("[우클릭]: 내용물 확인").style(MX.STYLE_NORMAL),
                Component.text("[Shift + 우클릭]: 설치").style(MX.STYLE_NORMAL)
        ));

        bsm.setBlockState(state);
        box.setItemMeta(bsm);
        return box;
    }
}
