package com.themrsung.mirae.command.home;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Set home command.
 */
public class SetHomeCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetHomeCommand() {
        super("sethome");
        setAliases(List.of(
                "집설정",
                "홈설정"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        if (args.length == 0) {
            Location here = player.getLocation();
            account.setHome(here);
            sender.sendMessage(HOME_SET_TO_HERE);
            return true;
        }

        String key = args[0];

        int currentHomeCount = account.getExtraHomeMap().size();
        int maxHomeCount = account.getMaxExtraHomes();

        boolean editing = account.getExtraHomeMap().containsKey(key);

        if (currentHomeCount >= maxHomeCount && !editing) {
            sender.sendMessage(Component.text("추가 홈 한도를 초과하였습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        Location here = player.getLocation();
        account.setExtraHome(key, here);
        sender.sendMessage(HOME_SET_TO_HERE);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> List.of("빈 칸으로 설정하시면 기본 홈이 설정되며, 이름을 입력하시면 해당 홈으로 지정됩니다.");
            default -> List.of();
        };
    }
}
