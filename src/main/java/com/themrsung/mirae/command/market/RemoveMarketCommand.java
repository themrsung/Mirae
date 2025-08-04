package com.themrsung.mirae.command.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.market.Market;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Remove market command.
 */
public class RemoveMarketCommand extends MiraeCommand {
    public RemoveMarketCommand() {
        super("removemarket");
        setAliases(List.of(
                "deletemarket"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/removemarket 고유번호").style(MX.STYLE_WARNING));
            return false;
        }

        UUID uniqueId;

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

        Mirae.getState().removeMarket(market);
        sender.sendMessage(Component.text("상점이 삭제되었습니다.").style(MX.STYLE_GOOD));
        return true;
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
            default -> List.of();
        };
    }
}
