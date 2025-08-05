package com.themrsung.mirae.upgrade.composition;

import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * Wooden tools to Netherite tools.
 */
public class WoodenToolToNetheriteRecipe implements UpgradeRecipe {
    private static final @NotNull EnumSet<Material> TOOLS = EnumSet.of(
            Material.WOODEN_SWORD,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_AXE,
            Material.WOODEN_SHOVEL,
            Material.WOODEN_HOE
    );

    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) && ticket != null) return null;

        if (!TOOLS.contains(left.getType())) return null;

        ItemStack uranium = CustomItem.URANIUM_RAW.getItem();
        if (!uranium.isSimilar(right)) return null;
        if (right.getAmount() != 1) return null;

        ItemMeta meta = left.getItemMeta();
        ItemStack result = switch (left.getType()) {
            case WOODEN_SWORD -> new ItemStack(Material.NETHERITE_SWORD);
            case WOODEN_PICKAXE -> new ItemStack(Material.NETHERITE_PICKAXE);
            case WOODEN_AXE -> new ItemStack(Material.NETHERITE_AXE);
            case WOODEN_SHOVEL -> new ItemStack(Material.NETHERITE_SHOVEL);
            case WOODEN_HOE -> new ItemStack(Material.NETHERITE_HOE);
            default -> null;
        };

        if (CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket)) {
            List<Component> lore = new ArrayList<>(Objects.requireNonNullElse(meta.lore(), List.of()));
            lore.add(SUCCESS_TICKET_IMPRINT);
            meta.lore(lore);
        }

        if (result != null) {
            result.setItemMeta(meta);
        }

        return result;
    }

    @Override
    public double getSuccessRate() {
        return 0.025;
    }
}
