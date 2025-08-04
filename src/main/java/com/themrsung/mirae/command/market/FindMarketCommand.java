package com.themrsung.mirae.command.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.market.Market;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Find market command.
 */
public class FindMarketCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public FindMarketCommand() {
        super("findmarket");
        setAliases(List.of(
                "searchmarket"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!player.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        List<Market> markets = new ArrayList<>();

        if (args.length < 1) {
            ItemStack item = player.getInventory().getItemInMainHand();
            markets.addAll(Mirae.getState().getMarkets().stream()
                    .filter(m -> m.getItem().isSimilar(item))
                    .toList());
        } else {
            String query = args[0];
            markets.addAll(Mirae.getState().getMarkets().stream()
                    .filter(m -> m.getName().toLowerCase().contains(query.toLowerCase()))
                    .toList());
        }

        sender.sendMessage(Component.text("검색된 시장 (" + markets.size() + "개): ").style(MX.STYLE_SPECIAL));
        markets.forEach(market -> {
            sender.sendMessage(Component.text("  - ").style(MX.STYLE_NORMAL)
                    .append(Component.text(market.getName()).style(MX.STYLE_SPECIAL)
                            .hoverEvent(HoverEvent.showText(Component.text("클릭하여 편집합니다...").style(MX.STYLE_NORMAL)))
                            .clickEvent(ClickEvent.runCommand("/editmarket " + market.getUniqueId()))));
        });

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getMarkets().stream()
                    .map(Market::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
