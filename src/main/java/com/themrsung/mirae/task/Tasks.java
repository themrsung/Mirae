package com.themrsung.mirae.task;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.task.abuse.AntiSpamBanTask;
import com.themrsung.mirae.task.economy.InterestPayoutTask;
import com.themrsung.mirae.task.economy.MarketUpdateTask;
import com.themrsung.mirae.task.player.ScoreboardTask;
import com.themrsung.mirae.task.server.NotifierTask;
import com.themrsung.mirae.task.state.AutoSaveTask;
import com.themrsung.mirae.task.state.MuteExpirationTask;
import com.themrsung.mirae.task.state.TransientVariableCleanupTask;
import com.themrsung.mirae.task.state.UpdateNameTask;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Tasks.
 */
public final class Tasks {
    public static final @NotNull MuteExpirationTask MUTE_EXPIRATION_TASK = new MuteExpirationTask();
    public static final @NotNull TransientVariableCleanupTask TRANSIENT_VARIABLE_CLEANUP_TASK = new TransientVariableCleanupTask();
    public static final @NotNull AntiSpamBanTask ANTI_SPAM_BAN_TASK = new AntiSpamBanTask();
    public static final @NotNull UpdateNameTask UPDATE_NAME_TASK = new UpdateNameTask();
    public static final @NotNull AutoSaveTask AUTO_SAVE_TASK = new AutoSaveTask();
    public static final @NotNull ScoreboardTask SCOREBOARD_TASK = new ScoreboardTask();
    public static final @NotNull NotifierTask NOTIFIER_TASK = new NotifierTask();
    public static final @NotNull InterestPayoutTask INTEREST_PAYOUT_TASK = new InterestPayoutTask();
    public static final @NotNull MarketUpdateTask MARKET_UPDATE_TASK = new MarketUpdateTask();

    /**
     * The set of all tasks.
     */
    private static final @NotNull Set<Runnable> TASKS = Set.of(
            MUTE_EXPIRATION_TASK,
            TRANSIENT_VARIABLE_CLEANUP_TASK,
            ANTI_SPAM_BAN_TASK,
            UPDATE_NAME_TASK,
            AUTO_SAVE_TASK,
            SCOREBOARD_TASK,
            NOTIFIER_TASK,
            INTEREST_PAYOUT_TASK,
            MARKET_UPDATE_TASK
    );

    /**
     * Returns the set of all tasks.
     *
     * @return The set of all tasks
     */
    public static @NotNull Set<Runnable> getTasks() {
        return TASKS;
    }

    /**
     * Registers every task.
     *
     * @param p The plugin instance
     * @param s The scheduler instance
     */
    public static void registerTasks(@NotNull Mirae p, @NotNull BukkitScheduler s) {
        // plugin, task, interval, delay

        s.scheduleSyncRepeatingTask(p, MUTE_EXPIRATION_TASK, 20, 200);
        s.scheduleSyncRepeatingTask(p, TRANSIENT_VARIABLE_CLEANUP_TASK, 20, 200);
        s.scheduleSyncRepeatingTask(p, ANTI_SPAM_BAN_TASK, 20, 200);
        s.scheduleSyncRepeatingTask(p, UPDATE_NAME_TASK, 20 * 60 * 10, 200);
        s.scheduleSyncRepeatingTask(p, AUTO_SAVE_TASK, 20 * 60 * 5, 20 * 60 * 5);
        s.scheduleSyncRepeatingTask(p, SCOREBOARD_TASK, 5, 5);
        s.scheduleSyncRepeatingTask(p, NOTIFIER_TASK, 20 * 60 * 5, 20 * 60 * 3);
        s.scheduleSyncRepeatingTask(p, INTEREST_PAYOUT_TASK, 20 * 60 * 10, 20 * 60 * 10);
        s.scheduleSyncRepeatingTask(p, MARKET_UPDATE_TASK, 50, 200);
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Tasks() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
