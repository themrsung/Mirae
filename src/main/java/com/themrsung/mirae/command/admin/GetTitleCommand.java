package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetTitleCommand extends MiraeCommand {
    public GetTitleCommand() {
        super("gettitle");
        setAliases(List.of(
                "titleitem"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/gettitle 칭호").style(MX.STYLE_WARNING));
            return false;
        }

        AccountTitle title = AccountTitle.getOrEmpty(args[0].toLowerCase());
        ItemStack item = title.generateItem();

        player.getInventory().addItem(item);
        sender.sendMessage(Component.text("칭호 \"").style(MX.STYLE_NORMAL)
                .append(title.getValue())
                .append(Component.text("\"을 생성했습니다.").style(MX.STYLE_NORMAL)));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> AccountTitle.getKeys().stream()
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
