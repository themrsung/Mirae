package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTier;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Item name command.
 */
public class ItemNameCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public ItemNameCommand() {
        super("itemname");
        setAliases(List.of(
                "itemrename",
                "아이템명",
                "아이템이름"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (!account.getTier().isAtLeast(AccountTier.GOLD)) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if (args.length < 1) {
            meta.displayName(null);
            meta.itemName(null);

            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);

            sender.sendMessage(Component.text("아이템명을 초기화했습니다.").style(MX.STYLE_GOOD));
            return true;
        }

        String rawName = String.join(" ", args);
        Component itemName = MiniMessage.miniMessage().deserialize(rawName).applyFallbackStyle(Style.style()
                .decoration(TextDecoration.ITALIC, false)
                .build());

        meta.displayName(itemName);
        meta.itemName(itemName);

        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);

        sender.sendMessage(Component.text("아이템명을 변경했습니다.").style(MX.STYLE_GOOD));
        return true;
    }
}
