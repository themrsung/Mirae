package com.themrsung.mirae.command.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.market.MarketCategory;
import com.themrsung.mirae.market.active.ActivePriceMarket;
import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Create market command.
 */
public class CreateMarketCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public CreateMarketCommand() {
        super("createmarket");
        setAliases(List.of(
                "newmarket"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (!player.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(Component.text("/createmarket 유형 이름 카테고리 [가격]"));
            return false;
        }

        String name = args[1];
        if (Mirae.getState().getMarkets().stream().anyMatch(m -> m.getName().equalsIgnoreCase(name))) {
            sender.sendMessage(NAME_ALREADY_TAKEN);
            return false;
        }

        String categoryQuery = args[2].toUpperCase();
        MarketCategory category;
        try {
            category = MarketCategory.valueOf(categoryQuery);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CANNOT_FIND_CATEGORY);
            return false;
        }

        double initialPrice = args.length > 3 ? MX.parseDouble(args[3]) : -1;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            sender.sendMessage(Component.text("손에 든 아이템이 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        Market market;

        String typeQuery = args[0];
        if (typeQuery.equalsIgnoreCase("active")) {
            market = new ActivePriceMarket(name, item, category, initialPrice);
        } else if (typeQuery.equalsIgnoreCase("fixed")) {
            market = new FixedPriceMarket(name, item, category);
            var fpm = (FixedPriceMarket) market;
            fpm.setBuyPrice(initialPrice);
            fpm.setSellPrice(initialPrice);
        } else {
            sender.sendMessage(Component.text("유효하지 않은 상점 유형입니다.").style(MX.STYLE_WARNING));
            return false;
        }

        Mirae.getState().addMarket(market);

        sender.sendMessage(Component.text("상점이 추가되었습니다.").style(MX.STYLE_GOOD)
                .append(Component.text(" [")).style(MX.STYLE_NORMAL)
                .append(Component.text(market.getUniqueId().toString())).style(MX.STYLE_NORMAL)
                .hoverEvent(HoverEvent.showText(Component.text("클릭하여 복사합니다...").style(MX.STYLE_NORMAL)))
                .clickEvent(ClickEvent.copyToClipboard(market.getUniqueId().toString()))
                .append(Component.text("]")));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Stream.of("active", "fixed").filter(s -> s.startsWith(args[0].toLowerCase())).toList();
            case 2 -> List.of("이름을 입력하세요...");
            case 3 -> Arrays.stream(MarketCategory.values())
                    .map(MarketCategory::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[2].toLowerCase()))
                    .toList();
            case 4 -> List.of("가격을 입력하세요...");
            default -> List.of();
        };
    }
}
