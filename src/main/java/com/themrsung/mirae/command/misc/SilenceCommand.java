package com.themrsung.mirae.command.misc;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Silence command.
 */
public class SilenceCommand extends MiraeCommand {
    /**
     * Silence.
     */
    public SilenceCommand() {
        super("silence");
        setAliases(List.of(
                "조용",
                "조용히",
                "음소거",
                "시끄러",
                "시끄러임마"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        player.stopAllSounds();
        sender.sendMessage(Component.text("조용하죠?").style(MX.STYLE_GOOD));
        return true;
    }
}
