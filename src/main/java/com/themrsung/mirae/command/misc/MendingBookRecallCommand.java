package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.item.EnchantedItemSupplier;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Mending book recall.
 */
public class MendingBookRecallCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public MendingBookRecallCommand() {
        super("mendingbookrecall");
        setAliases(List.of(
                "수선책수리"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        ItemStack oldBook = EnchantedItemSupplier.MENDING_BOOK_LEGACY.getItem();
        ItemStack newBook = EnchantedItemSupplier.MENDING_BOOK.getItem();

        int count = MX.countItems(player.getInventory(), oldBook);
        if (count <= 0) {
            sender.sendMessage(Component.text("불량 수선책이 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        int failed = MX.takeItems(player.getInventory(), oldBook);
        int taken = count - failed;

        newBook.setAmount(taken);
        MX.giveItems(player, newBook);

        sender.sendMessage(Component.text("인벤토리에 있는 불량 수선책을 전부 수리하였습니다.").style(MX.STYLE_GOOD));

        return true;
    }
}
