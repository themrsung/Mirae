package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Scoreboard toggle command.
 */
public class ScoreboardToggleCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public ScoreboardToggleCommand() {
        super("scoreboardtoggle");
        setAliases(List.of(
                "sbtoggle",
                "sb",
                "스코어보드"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        boolean hide = !account.hideScoreboard();

        account.setHideScoreboard(hide);

        if (hide) {
            sender.sendMessage(Component.text("스코어보드를 표시하지 않습니다.").style(MX.STYLE_GOOD));
        } else {
            sender.sendMessage(Component.text("스코어보드를 표시합니다.").style(MX.STYLE_GOOD));
        }

        return true;
    }
}
