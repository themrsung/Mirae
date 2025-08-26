package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Create account command.
 */
public class CreateAccountCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public CreateAccountCommand() {
        super("createaccount");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/createaccount 유저네임").style(MX.STYLE_WARNING));
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        Account account = Account.createAccount(player.getUniqueId());
        if (!Mirae.getState().addAccount(account)) {
            sender.sendMessage(INTERNAL_ERROR);
            return false;
        }

        sender.sendMessage(Component.text("계정을 생성했습니다."));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> List.of("유저네임");
            default -> List.of();
        };
    }
}
