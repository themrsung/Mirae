package com.themrsung.mirae.task;

import com.themrsung.mirae.Mirae;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class Tasks {
    private static final @NotNull MuteExpirationTask MUTE_EXPIRATION_TASK = new MuteExpirationTask();
    private static final @NotNull TransientVariableCleanupTask TRANSIENT_VARIABLE_CLEANUP_TASK = new TransientVariableCleanupTask();

    public static @NotNull MuteExpirationTask getMuteExpirationTask() {
        return MUTE_EXPIRATION_TASK;
    }

    public static @NotNull TransientVariableCleanupTask getTransientVariableCleanupTask() {
        return TRANSIENT_VARIABLE_CLEANUP_TASK;
    }

    public static @NotNull Set<Runnable> getTasks() {
        return Set.of(
                MUTE_EXPIRATION_TASK,
                TRANSIENT_VARIABLE_CLEANUP_TASK
        );
    }

    public static void registerTasks(@NotNull Mirae p, @NotNull BukkitScheduler s) {
        // plugin, task, interval, delay

        s.scheduleSyncRepeatingTask(p, MUTE_EXPIRATION_TASK, 20, 200);
        s.scheduleSyncRepeatingTask(p, TRANSIENT_VARIABLE_CLEANUP_TASK, 20, 200);
    }

    private Tasks() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
