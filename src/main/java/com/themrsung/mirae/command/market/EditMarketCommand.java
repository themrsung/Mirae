package com.themrsung.mirae.command.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.market.active.ActivePriceMarket;
import com.themrsung.mirae.market.active.VolatilityLevel;
import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Edit market command.
 */
public class EditMarketCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public EditMarketCommand() {
        super("editmarket");
        setAliases(List.of(
                "modifymarket"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        UUID uniqueId;

        if (args.length == 1) {
            try {
                uniqueId = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(CANNOT_FIND_MARKET);
                return false;
            }

            Market market = Mirae.getState().getMarket(uniqueId);
            if (market == null) {
                sender.sendMessage(CANNOT_FIND_MARKET);
                return false;
            }

            sender.sendMessage(Component.text("시장 관리: ").style(MX.STYLE_NORMAL)
                    .append(Component.text(market.getName())).style(MX.STYLE_SPECIAL));

            switch (market.getType()) {
                case ACTIVE_PRICE -> {
                    ActivePriceMarket apm = (ActivePriceMarket) market;
                    sender.sendMessage(Component.text("  - 기본 가격: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(apm.getDefaultPrice())).style(MX.STYLE_GOOD)
                                    .hoverEvent(HoverEvent.showText(Component.text("클릭하여 수정합니다...").style(MX.STYLE_NORMAL)))
                                    .clickEvent(ClickEvent.suggestCommand("/editmarket " + market.getUniqueId() + " defaultprice "))));

                    sender.sendMessage(Component.text("  - 변동성: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(apm.getVolatilityLevel().toString()).style(MX.STYLE_GOOD)
                                    .hoverEvent(HoverEvent.showText(Component.text("클릭하여 수정합니다...").style(MX.STYLE_NORMAL)))
                                    .clickEvent(ClickEvent.suggestCommand("/editmarket " + market.getUniqueId() + " volatilitylevel "))));
                }

                case FIXED_PRICE -> {
                    FixedPriceMarket fpm = (FixedPriceMarket) market;
                    sender.sendMessage(Component.text("  - 판매가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(fpm.getBuyPrice(1).price())).style(MX.STYLE_GOOD)
                                    .hoverEvent(HoverEvent.showText(Component.text("클릭하여 수정합니다...").style(MX.STYLE_NORMAL)))
                                    .clickEvent(ClickEvent.suggestCommand("/editmarket " + market.getUniqueId() + " buyprice "))));

                    sender.sendMessage(Component.text("  - 매입가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(fpm.getSellPrice(1).price())).style(MX.STYLE_GOOD)
                                    .hoverEvent(HoverEvent.showText(Component.text("클릭하여 수정합니다...").style(MX.STYLE_NORMAL)))
                                    .clickEvent(ClickEvent.suggestCommand("/editmarket " + market.getUniqueId() + " sellprice "))));
                }
            }

            sender.sendMessage(Component.text("[삭제하기]").style(MX.STYLE_ERROR)
                    .clickEvent(ClickEvent.suggestCommand("/removemarket " + market.getUniqueId())));

            return true;
        } else if (args.length < 3) {
            sender.sendMessage(Component.text("/editmarket 고유번호"));
            return false;
        }

        try {
            uniqueId = UUID.fromString(args[0]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CANNOT_FIND_MARKET);
            return false;
        }

        Market market = Mirae.getState().getMarket(uniqueId);
        if (market == null) {
            sender.sendMessage(CANNOT_FIND_MARKET);
            return false;
        }

        String param = args[1].toLowerCase();
        return switch (market.getType()) {
            case ACTIVE_PRICE -> {
                ActivePriceMarket apm = (ActivePriceMarket) market;
                yield switch (param) {
                    case "defaultprice" -> {
                        double price = MX.parseDouble(args[2]);
                        apm.setDefaultPrice(price);
                        sender.sendMessage(EDIT_SUCCESSFUL);
                        yield true;
                    }

                    case "volatilitylevel" -> {
                        VolatilityLevel level;
                        try {
                            level = VolatilityLevel.valueOf(args[2].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(INTERNAL_ERROR);
                            yield false;
                        }

                        apm.setVolatilityLevel(level);
                        sender.sendMessage(EDIT_SUCCESSFUL);
                        yield true;
                    }

                    default -> {
                        sender.sendMessage(INTERNAL_ERROR);
                        yield false;
                    }
                };
            }

            case FIXED_PRICE -> {
                FixedPriceMarket fpm = (FixedPriceMarket) market;
                yield switch (param) {
                    case "buyprice" -> {
                        double price = MX.parseDouble(args[2]);
                        fpm.setBuyPrice(price);
                        sender.sendMessage(EDIT_SUCCESSFUL);
                        yield true;
                    }

                    case "sellprice" -> {
                        double price = MX.parseDouble(args[2]);
                        fpm.setSellPrice(price);
                        sender.sendMessage(EDIT_SUCCESSFUL);
                        yield true;
                    }

                    default -> {
                        sender.sendMessage(INTERNAL_ERROR);
                        yield false;
                    }
                };
            }

            default -> {
                sender.sendMessage(INTERNAL_ERROR);
                yield false;
            }
        };
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if (!sender.isOp()) return List.of(); // Save resources

        return switch (args.length) {
            case 1 -> Mirae.getState().getMarkets().stream()
                    .map(Market::getUniqueId)
                    .map(UUID::toString)
                    .filter(s -> s.startsWith(args[0]))
                    .toList();
            case 2 -> {
                try {
                    UUID uniqueId = UUID.fromString(args[0]);
                    Market market = Objects.requireNonNull(Mirae.getState().getMarket(uniqueId));
                    yield switch (market.getType()) {
                        case ACTIVE_PRICE -> Stream.of("defaultprice", "volatilitylevel")
                                .filter(s -> s.startsWith(args[1].toLowerCase()))
                                .toList();
                        case FIXED_PRICE -> Stream.of("buyprice", "sellprice")
                                .filter(s -> s.startsWith(args[1].toLowerCase()))
                                .toList();
                        default -> List.of();
                    };
                } catch (RuntimeException e) {
                    yield List.of();
                }
            }

            case 3 -> {
                if (List.of("defaultprice", "buyprice", "sellprice").contains(args[1].toLowerCase())) {
                    yield List.of("금액을 입력하세요...");
                } else if (args[1].equalsIgnoreCase("volatilitylevel")) {
                    yield Arrays.stream(VolatilityLevel.values())
                            .map(VolatilityLevel::toString)
                            .map(String::toLowerCase)
                            .filter(s -> s.startsWith(args[2].toLowerCase()))
                            .toList();
                }

                yield List.of();
            }

            default -> List.of();
        };
    }
}
