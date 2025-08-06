package com.themrsung.mirae.listener.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Skill restriction listener.
 */
public class SkillRestrictionListener implements Listener {
    public static final long NETHERITE_PICKAXE_MINING_LEVEL = 10;

    @EventHandler
    public void onNetheritePickaxeUsed(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType() != Material.NETHERITE_PICKAXE) return;

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (account.hasSkillLevel(SkillType.MINING, NETHERITE_PICKAXE_MINING_LEVEL)) return;

        e.setCancelled(true);
        player.sendMessage(Component.text("네더라이트 곡괭이는 ").style(MX.STYLE_NORMAL)
                .append(SkillType.MINING.getDisplayName())
                .appendSpace()
                .append(Component.text(NETHERITE_PICKAXE_MINING_LEVEL + "레벨").style(MX.STYLE_SPECIAL))
                .append(Component.text(" 이상부터 사용 가능합니다.").style(MX.STYLE_NORMAL)));
    }
}
