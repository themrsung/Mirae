package com.themrsung.mirae.listener.enchant;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.enchant.CustomEnchantment;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ThreeByThreeMiningListener implements Listener {
    private static final @NotNull EnumSet<Material> THREE_BY_THREE_BLACKLIST;

    static {
        var blacklist = new HashSet<Material>();

        blacklist.add(Material.BEDROCK);
        blacklist.add(Material.BARRIER);
        blacklist.add(Material.VAULT);
        blacklist.add(Material.REINFORCED_DEEPSLATE);
        blacklist.add(Material.NOTE_BLOCK); // Used by ItemsAdder

        THREE_BY_THREE_BLACKLIST = EnumSet.copyOf(blacklist);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (!CustomEnchantment.Value.THREE_BY_THREE_MINING.hasEnchant(item)) return;

        Location location = e.getBlock().getLocation();
        Player player = e.getPlayer();
        BlockFace facing = player.getTargetBlockFace(5);

        if (facing == null) return;

        Set<Location> locationsToBreak = new HashSet<>();

        switch (facing) {
            case UP, DOWN -> {
                locationsToBreak.add(location.clone().add(-1, 0, -1));
                locationsToBreak.add(location.clone().add(-1, 0, 0));
                locationsToBreak.add(location.clone().add(-1, 0, 1));
                locationsToBreak.add(location.clone().add(0, 0, -1));
                locationsToBreak.add(location.clone().add(0, 0, 1));
                locationsToBreak.add(location.clone().add(1, 0, -1));
                locationsToBreak.add(location.clone().add(1, 0, 0));
                locationsToBreak.add(location.clone().add(1, 0, 1));
            }

            case NORTH, SOUTH -> {
                locationsToBreak.add(location.clone().add(-1, -1, 0));
                locationsToBreak.add(location.clone().add(-1, 0, 0));
                locationsToBreak.add(location.clone().add(-1, 1, 0));
                locationsToBreak.add(location.clone().add(0, -1, 0));
                locationsToBreak.add(location.clone().add(0, 1, 0));
                locationsToBreak.add(location.clone().add(1, -1, 0));
                locationsToBreak.add(location.clone().add(1, 0, 0));
                locationsToBreak.add(location.clone().add(1, 1, 0));
            }

            case EAST, WEST -> {
                locationsToBreak.add(location.clone().add(0, -1, -1));
                locationsToBreak.add(location.clone().add(0, -1, 0));
                locationsToBreak.add(location.clone().add(0, -1, 1));
                locationsToBreak.add(location.clone().add(0, 0, -1));
                locationsToBreak.add(location.clone().add(0, 0, 1));
                locationsToBreak.add(location.clone().add(0, 1, -1));
                locationsToBreak.add(location.clone().add(0, 1, 0));
                locationsToBreak.add(location.clone().add(0, 1, 1));
            }
        }

        GriefPrevention gp = MX.getGriefPrevention();

        locationsToBreak.forEach(loc -> {
            Block block = loc.getBlock();
            if (THREE_BY_THREE_BLACKLIST.contains(block.getType())) return;

            Claim claim = gp.dataStore.getClaimAt(block.getLocation(), false, null);
            if (claim != null) {
                Supplier<String> result = claim.checkPermission(e.getPlayer(), ClaimPermission.Build, e);
                if (result != null) {
                    player.sendMessage(Component.text(result.get()).style(MX.STYLE_ERROR));
                    return;
                }
            }

            block.breakNaturally(player.getInventory().getItemInMainHand(), true);
        });
    }
}
