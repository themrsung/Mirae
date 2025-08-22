package com.themrsung.mirae.upgrade.enchant;

import com.themrsung.mirae.enchant.CustomEnchantment;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Custom enchantment application.
 */
public class CustomEnchantApplicationRecipe implements UpgradeRecipe {
    private static final EnumSet<Material> PICKAXES = EnumSet.of(
            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
    );

    private static final EnumSet<Material> LAUNCHERS = EnumSet.of(
            Material.BOW,
            Material.CROSSBOW
    );

    private static boolean isShield(@NotNull ItemStack item) {
        if (item.getType() == Material.SHIELD) return true;
        if (CustomItem.CAPTAIN_SHIELD.isItem(item)) return true;

        return false;
    }

    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (!(left.getItemMeta() instanceof Damageable)) return null;
        if (right.getType() != Material.ENCHANTED_BOOK) return null;

        ItemStack result = left.clone();
        AtomicBoolean didEnchant = new AtomicBoolean(false);

        CustomEnchantment.getEnchantments(right)
                .forEach((e, l) -> {
                    // Cannot apply 3x3 mining or super shovel to non-pickaxe
                    if ((e == CustomEnchantment.Value.SUPER_SHOVEL || e == CustomEnchantment.Value.THREE_BY_THREE_MINING) &&
                            !PICKAXES.contains(left.getType())) return;

                    // Cannot apply seeker bow to non-launcher
                    if (e == CustomEnchantment.Value.SEEKER_BOW &&
                            !LAUNCHERS.contains(left.getType())) return;

                    // Cannot apply EMP to non-shield
                    if (e == CustomEnchantment.Value.EMP_SHIELD &&
                            !isShield(left)) return;

                    // Skip if item already has enchant or greater
                    if (e.getEnchantLevel(left) >= l) return;

                    e.setEnchantLevel(result, l);
                    didEnchant.set(true);
                });

        if (!didEnchant.get()) return null;
        return result;
    }

    @Override
    public double getSuccessRate() {
        return 1;
    }
}
