package com.themrsung.mirae.upgrade.weapon;

import com.themrsung.mirae.enchant.CustomEnchantment;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * BeamSword.
 */
public class ColoredBeamSwordRecipe implements UpgradeRecipe {
    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!CustomItem.CLOSED_BEAM_SWORD.isItem(left)) return null;
        if (right.getAmount() != 1) return null;

        ItemStack saber = switch (right.getType()) {
            case REDSTONE -> CustomItem.RED_BEAM_SWORD.getItem();
            case LAPIS_LAZULI -> CustomItem.BLUE_BEAM_SWORD.getItem();
            case EMERALD -> CustomItem.GREEN_BEAM_SWORD.getItem();
            default -> null;
        };

        if (saber == null) return null;

        // Lore

        boolean hasLore;
        List<Component> lore = left.getItemMeta().lore();

        hasLore = lore != null && !lore.isEmpty();

        if (hasLore) {
            ItemMeta meta = saber.getItemMeta();
            meta.lore(lore);
            saber.setItemMeta(meta);
        }

        // Enchants

        Map<Enchantment, Integer> enchantments = left.getItemMeta().getEnchants();
        if (!enchantments.isEmpty()) {
            ItemMeta meta = saber.getItemMeta();
            enchantments.forEach((e, l) -> meta.addEnchant(e, l, true));
            saber.setItemMeta(meta);
        }

        // Custom Enchants

        CustomEnchantment.getEnchantments(left)
                .forEach((e, l) -> e.setEnchantLevel(saber, l));

        return saber;
    }

    @Override
    public double getSuccessRate() {
        return 1;
    }
}
