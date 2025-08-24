package com.themrsung.mirae.task;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.task.abuse.AntiSpamBanTask;
import com.themrsung.mirae.task.abuse.TransferAmountMonitorTask;
import com.themrsung.mirae.task.economy.InterestPayoutTask;
import com.themrsung.mirae.task.economy.MarketUpdateTask;
import com.themrsung.mirae.task.item.ArrowPropulsionTask;
import com.themrsung.mirae.task.item.ArrowSeekerTask;
import com.themrsung.mirae.task.item.EmpShieldTask;
import com.themrsung.mirae.task.item.MagnetTask;
import com.themrsung.mirae.task.player.ElytraDurabilityTask;
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
    public static final @NotNull TransferAmountMonitorTask TRANSFER_AMOUNT_MONITOR_TASK = new TransferAmountMonitorTask();
    public static final @NotNull ElytraDurabilityTask ELYTRA_DURABILITY_TASK = new ElytraDurabilityTask();
    public static final @NotNull MagnetTask MAGNET_TASK = new MagnetTask();
    public static final @NotNull ArrowSeekerTask ARROW_SEEKER_TASK = new ArrowSeekerTask();
    public static final @NotNull EmpShieldTask EMP_SHIELD_TASK = new EmpShieldTask();
    public static final @NotNull ArrowPropulsionTask ARROW_PROPULSION_TASK = new ArrowPropulsionTask();

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
            MARKET_UPDATE_TASK,
            TRANSFER_AMOUNT_MONITOR_TASK,
            ELYTRA_DURABILITY_TASK,
            MAGNET_TASK,
            ARROW_SEEKER_TASK,
            EMP_SHIELD_TASK,
            ARROW_PROPULSION_TASK
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
        // plugin, task, delay, interval
        s.runTaskTimer(p, MUTE_EXPIRATION_TASK, 200, 20);
        s.runTaskTimer(p, TRANSIENT_VARIABLE_CLEANUP_TASK, 200, 300);
        s.runTaskTimer(p, ANTI_SPAM_BAN_TASK, 200, 20);
        s.runTaskTimer(p, UPDATE_NAME_TASK, 200, 20 * 60 * 10);
        s.runTaskTimer(p, AUTO_SAVE_TASK, 20 * 60 * 5, 20 * 60 * 5);
        s.runTaskTimer(p, SCOREBOARD_TASK, 5, 5);
        s.runTaskTimer(p, NOTIFIER_TASK, 20 * 60 * 3, 20 * 60 * 5);
        s.runTaskTimer(p, INTEREST_PAYOUT_TASK, 20 * 60 * 10, 20 * 60 * 10);
        s.runTaskTimer(p, MARKET_UPDATE_TASK, 200, 50);
        s.runTaskTimer(p, TRANSFER_AMOUNT_MONITOR_TASK, 200, 300);
        s.runTaskTimer(p, ELYTRA_DURABILITY_TASK, 100, 5);
        s.runTaskTimer(p, MAGNET_TASK, 200, 7);
        s.runTaskTimer(p, ARROW_SEEKER_TASK, 200, 4);
        s.runTaskTimer(p, EMP_SHIELD_TASK, 200, 2);
        s.runTaskTimer(p, ARROW_PROPULSION_TASK, 200, 3);
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
