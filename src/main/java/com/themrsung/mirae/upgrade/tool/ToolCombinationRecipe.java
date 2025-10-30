package com.themrsung.mirae.upgrade.tool;

import com.themrsung.mirae.upgrade.UpgradeRecipe;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Recipe that combines two identical tools, merging durability and enchantments similarly to vanilla anvils.
 */
public class ToolCombinationRecipe implements UpgradeRecipe {
    private static final double BONUS_DURABILITY_RATIO = 0.12d;

    @Override
    public @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket) {
        if (ticket != null) return null;
        if (left.getAmount() != 1 || right.getAmount() != 1) return null;

        Material material = left.getType();
        if (material != right.getType()) return null;
        if (material.getMaxDurability() <= 0) return null;

        ItemMeta leftMeta = left.getItemMeta();
        ItemMeta rightMeta = right.getItemMeta();
        if (!(leftMeta instanceof Damageable) || !(rightMeta instanceof Damageable)) return null;

        Damageable leftDamageable = (Damageable) leftMeta;
        Damageable rightDamageable = (Damageable) rightMeta;

        ItemStack result = left.clone();
        result.setAmount(1);

        ItemMeta resultMeta = result.getItemMeta();
        if (!(resultMeta instanceof Damageable)) return null;
        Damageable resultDamageable = (Damageable) resultMeta;

        int maxDurability = material.getMaxDurability();
        int leftDurability = Math.max(0, maxDurability - leftDamageable.getDamage());
        int rightDurability = Math.max(0, maxDurability - rightDamageable.getDamage());
        int bonus = (int) Math.round(maxDurability * BONUS_DURABILITY_RATIO);
        int newDurability = Math.min(maxDurability, leftDurability + rightDurability + bonus);
        int newDamage = Math.max(0, maxDurability - newDurability);
        resultDamageable.setDamage(newDamage);

        Map<Enchantment, Integer> combinedEnchantments = new HashMap<>(result.getEnchantments());
        Map<Enchantment, Integer> rightEnchantments = right.getEnchantments();
        for (Map.Entry<Enchantment, Integer> entry : rightEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int rightLevel = entry.getValue();
            int leftLevel = combinedEnchantments.getOrDefault(enchantment, 0);

            int newLevel;
            if (leftLevel == rightLevel) {
                newLevel = Math.min(leftLevel + 1, enchantment.getMaxLevel());
            } else {
                newLevel = Math.max(leftLevel, rightLevel);
            }

            if (newLevel > 0) {
                combinedEnchantments.put(enchantment, newLevel);
            }
        }

        Set<Enchantment> existing = new HashSet<>(resultMeta.getEnchants().keySet());
        for (Enchantment enchantment : existing) {
            resultMeta.removeEnchant(enchantment);
        }

        for (Map.Entry<Enchantment, Integer> entry : combinedEnchantments.entrySet()) {
            resultMeta.addEnchant(entry.getKey(), entry.getValue(), false);
        }

        result.setItemMeta(resultMeta);
        return result;
    }
}
