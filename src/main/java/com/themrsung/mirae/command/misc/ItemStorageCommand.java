package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.gui.storage.ItemStorageMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Opens the personal item storage GUI.
 */
public final class ItemStorageCommand extends MiraeCommand {
    public ItemStorageCommand() {
        super("storage");
        setDescription("개인 창고를 엽니다.");
        setUsage("/storage");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return true;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        new ItemStorageMenu(player, account).openGUI();
        return true;
    }
}
