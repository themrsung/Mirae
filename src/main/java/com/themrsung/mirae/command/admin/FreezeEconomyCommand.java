package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Freeze economy command.
 */
public class FreezeEconomyCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public FreezeEconomyCommand() {
        super("freezeeconomy");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("지금... 뭐하시는 거죠?").style(MX.STYLE_WARNING));
            return false;
        }

        boolean shouldFreeze = !Mirae.getState().isEconomyFrozen();
        Mirae.getState().setEconomyFrozen(shouldFreeze);

        sender.sendMessage(Component.text("경제 동결 상태를 변경했습니다. 현재 상태: ").style(MX.STYLE_NORMAL)
                .append(shouldFreeze ? Component.text("동결").style(MX.STYLE_ERROR) : Component.text("정상").style(MX.STYLE_GOOD)));
        return true;
    }
}
