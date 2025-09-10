package com.themrsung.mirae.listener.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.skill.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

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

    @EventHandler
    public void onWheatHarvested(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Block block = e.getBlock();
        if (block.getType() != Material.WHEAT) return;

        Ageable age = (Ageable) block.getBlockData();
        if (age.getAge() != age.getMaximumAge()) return;

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        long farmingSkillLevel = account.getSkillLevel(SkillType.FARMING);
        long adjustedLevel = farmingSkillLevel - 30;
        if (adjustedLevel < 0) return;

        int additionalDrops = (int) Math.floorDiv(adjustedLevel, 20) + 1;
        int finalDrops = Math.min(additionalDrops + 1, 16);

        ItemStack tool = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = block.getDrops(tool, player);

        ItemStack wheatToDrop = ItemStack.of(Material.WHEAT, finalDrops);

        e.setDropItems(false);
        Bukkit.getScheduler().runTaskLater(Mirae.getInstance(), () -> {
            for (ItemStack drop : drops) {
                if (drop.getType() != Material.WHEAT) {
                    block.getWorld().dropItem(block.getLocation(), drop);
                }
            }
            block.getWorld().dropItem(block.getLocation(), wheatToDrop);
        }, 1);
    }
}
