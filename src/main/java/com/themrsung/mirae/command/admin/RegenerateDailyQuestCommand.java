package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.task.Tasks;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Regenerates the daily quest chest on demand.
 */
public class RegenerateDailyQuestCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public RegenerateDailyQuestCommand() {
        super("dailyquestregen");
        setAliases(List.of(
                "regendailyquest",
                "dailyquestregenerate"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        boolean regenerated = Tasks.DAILY_QUEST_TASK.regenerateQuest();
        if (!regenerated) {
            sender.sendMessage(Component.text("일일 퀘스트 상자를 재생성하지 못했습니다. 콘솔 로그를 확인하세요.").style(MX.STYLE_ERROR));
            return false;
        }

        Location location = Mirae.getState().getDailyQuestLocation();
        Component message = Component.empty()
                .append(Component.text("일일 퀘스트 상자를 재생성했습니다. ").style(MX.STYLE_GOOD));

        if (location != null) {
            message = message
                    .append(Component.text("힌트: X ").style(MX.STYLE_NORMAL))
                    .append(Component.text(Integer.toString(location.getBlockX())).style(MX.STYLE_GOOD))
                    .append(Component.text(", Z ").style(MX.STYLE_NORMAL))
                    .append(Component.text(Integer.toString(location.getBlockZ())).style(MX.STYLE_GOOD));
        } else {
            message = message.append(Component.text("힌트를 제공할 수 없습니다.").style(MX.STYLE_WARNING));
        }

        sender.sendMessage(message);
        return true;
    }
}
