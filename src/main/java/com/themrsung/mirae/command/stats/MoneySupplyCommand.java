package com.themrsung.mirae.command.stats;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Money supply command.
 */
public class MoneySupplyCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public MoneySupplyCommand() {
        super("moneysupply");
        setAliases(List.of(
                "m1",
                "M1",
                "통화량"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("서버 총 통화량: ").style(MX.STYLE_NORMAL)
                .append(Component.text(MX.formatBalance(Mirae.getState().getMoneySupply())).style(MX.STYLE_SPECIAL)));
        return true;
    }
}
