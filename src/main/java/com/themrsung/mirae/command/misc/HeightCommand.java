package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Height command.
 */
public class HeightCommand extends MiraeCommand {
    public static final int MINIMUM_HEIGHT = 150;
    public static final int MAXIMUM_HEIGHT = 225;

    /**
     * Creates a new command.
     */
    public HeightCommand() {
        super("height");
        setAliases(List.of(
                "setheight",
                "키",
                "키설정"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        int height;

        try {
            height = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("키를 확인해주세요.").style(MX.STYLE_WARNING));
            return false;
        }

        if (height < MINIMUM_HEIGHT || height > MAXIMUM_HEIGHT) {
            sender.sendMessage(Component.text("키는 " + MINIMUM_HEIGHT + "cm 이상, " + MAXIMUM_HEIGHT + "cm 이하여야 합니다.").style(MX.STYLE_WARNING));
            return false;
        }

        double scale = height / 200d;

        String command = "attribute " + player.getName() + " minecraft:scale base set " + scale;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        sender.sendMessage(Component.text("키를 ").style(MX.STYLE_NORMAL)
                .append(Component.text(height + "cm").style(MX.STYLE_SPECIAL))
                .append(Component.text("으로 설정했습니다.").style(MX.STYLE_NORMAL)));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> List.of("키를 cm 단위로 입력하세요...");
            default -> List.of();
        };
    }
}
