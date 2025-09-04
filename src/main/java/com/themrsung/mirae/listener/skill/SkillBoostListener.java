package com.themrsung.mirae.listener.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.skill.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Skill boost listener.
 */
public class SkillBoostListener implements Listener {
    @EventHandler
    public void onNetheriteMined(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Block block = e.getBlock();
        if (block.getType() != Material.ANCIENT_DEBRIS) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack tool = player.getInventory().getItemInMainHand();
        ItemMeta meta = tool.getItemMeta();

        if (meta.hasEnchant(Enchantment.SILK_TOUCH)) return;

        long miningSkillLevel = account.getSkillLevel(SkillType.MINING);
        long adjustedLevel = miningSkillLevel - 30;
        if (adjustedLevel < 0) return;

        int additionalDrops = (int) Math.floorDiv(adjustedLevel, 20) + 1;
        int finalDrops = Math.min(additionalDrops + 1, 16);

        ItemStack itemsToDrop = ItemStack.of(Material.NETHERITE_SCRAP, finalDrops);

        e.setDropItems(false);
        Bukkit.getScheduler().runTaskLater(Mirae.getInstance(), () -> {
            block.getWorld().dropItem(block.getLocation(), itemsToDrop);
        }, 1);
    }
}
