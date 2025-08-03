package com.themrsung.mirae.command.social;

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
 * Local chat toggle command.
 */
public class LocalChatCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public LocalChatCommand() {
        super("localchat");
        setAliases(List.of(
                "local",
                "l",
                "global",
                "g",
                "지역",
                "지역채팅"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        boolean local = !account.inLocalChat();

        account.setLocalChat(local);

        if (local) {
            sender.sendMessage(Component.text("지역채팅을 활성화했습니다.").style(MX.STYLE_GOOD));
        } else {
            sender.sendMessage(Component.text("지역채팅을 비활성화했습니다.").style(MX.STYLE_GOOD));
        }

        return true;
    }
}
