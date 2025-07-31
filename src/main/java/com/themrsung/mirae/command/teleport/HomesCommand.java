package com.themrsung.mirae.command.teleport;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.util.Coordinate;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomesCommand extends MiraeCommand {
    public HomesCommand() {
        super("homes");
        setAliases(List.of(
                "homelist",
                "홈리스트",
                "집목록",
                "집보기",
                "홈목록",
                "홈보기"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Location mainHome = account.getHome();

        sender.sendMessage(Component.text("홈 목록:").style(MX.STYLE_SPECIAL));
        sender.sendMessage(Component.text("  - 주 주소지: ").style(MX.STYLE_NORMAL)
                .append(Component.text(
                        mainHome != null ? new Coordinate(mainHome).toString() : "없음"
                ).style(MX.STYLE_SPECIAL)));

        account.getExtraHomeMap().forEach((key, home) -> {
            sender.sendMessage(Component.text("  - " + key + ":").style(MX.STYLE_NORMAL)
                    .append(Component.text(new Coordinate(home).toString()).style(MX.STYLE_SPECIAL)));
        });

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return super.tabComplete(sender, label, args);
    }
}
