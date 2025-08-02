package com.themrsung.mirae.command.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTier;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NicknameCommand extends MiraeCommand {
    public NicknameCommand() {
        super("nickname");
        setAliases(List.of(
                "nick",
                "닉네임",
                "닉",
                "별명",
                "이름"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        if (!account.getTier().isAtLeast(AccountTier.GREEN)) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            account.setDisplayName(null);
            sender.sendMessage(NICKNAME_RESET);
            return false;
        }

        String messageRaw = String.join(" ", args);

        Component displayName;

        if (account.getTier().isAtLeast(AccountTier.GOLD)) {
            displayName = MiniMessage.miniMessage().deserialize(messageRaw.substring(0, Math.min(messageRaw.length(), 50)))
                    .applyFallbackStyle(MX.STYLE_SPECIAL);
        } else {
            displayName = Component.text(messageRaw.substring(0, Math.min(messageRaw.length(), 10)))
                    .style(MX.STYLE_SPECIAL);
        }

        account.setDisplayName(displayName);
        sender.sendMessage(NICKNAME_SET);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return super.tabComplete(sender, label, args);
    }
}
