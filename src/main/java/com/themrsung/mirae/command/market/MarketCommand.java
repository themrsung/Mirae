package com.themrsung.mirae.command.market;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.GUI;
import com.themrsung.mirae.gui.market.MarketMenu;
import com.themrsung.mirae.market.MarketCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Market command.
 */
public class MarketCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public MarketCommand() {
        super("market");
        setAliases(List.of(
                "markets",
                "shop",
                "shops",
                "시장",
                "상점"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (args.length < 1) {
            GUI menu = new MarketMenu(player);
            menu.openGUI();

            return true;
        }

        String query = args[0].toUpperCase();
        MarketCategory category;

        try {
            category = MarketCategory.valueOf(query);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CANNOT_FIND_CATEGORY);
            return false;
        }

        GUI menu = new MarketMenu(player, category);
        menu.openGUI();
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Arrays.stream(MarketCategory.values())
                    .map(MarketCategory::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }
}
