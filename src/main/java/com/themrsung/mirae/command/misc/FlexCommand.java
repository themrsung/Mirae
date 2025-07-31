package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlexCommand extends MiraeCommand {
    public FlexCommand() {
        super("flex");
        setAliases(List.of(
                "brag",
                "플렉스",
                "자랑"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            sender.sendMessage(Component.text("손에 든 아이템이 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        Component displayName = meta.displayName();
        Component display = (displayName != null ? displayName : Component.text(MX.getKoreanMaterialName(item.getType())))
                .hoverEvent(item.asHoverEvent());

        Bukkit.broadcast(account.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님의 아이템: ").style(MX.STYLE_NORMAL))
                .append(display));

        return super.execute(sender, label, args);
    }
}
