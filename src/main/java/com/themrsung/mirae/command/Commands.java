package com.themrsung.mirae.command;

import com.themrsung.mirae.command.teleport.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Commands.
 */
public final class Commands {
    private static final @NotNull SpawnCommand SPAWN_COMMAND = new SpawnCommand();
    private static final @NotNull BackCommand BACK_COMMAND = new BackCommand();
    private static final @NotNull SetSpawnCommand SET_SPAWN_COMMAND = new SetSpawnCommand();
    private static final @NotNull HomeCommand HOME_COMMAND = new HomeCommand();
    private static final @NotNull SetHomeCommand SET_HOME_COMMAND = new SetHomeCommand();
    private static final @NotNull DeleteHomeCommand DELETE_HOME_COMMAND = new DeleteHomeCommand();
    private static final @NotNull HomesCommand HOMES_COMMAND = new HomesCommand();

    private static final @NotNull Set<MiraeCommand> ALL_COMMANDS = Set.of(
            SPAWN_COMMAND,
            BACK_COMMAND,
            SET_SPAWN_COMMAND,
            HOME_COMMAND,
            SET_HOME_COMMAND,
            DELETE_HOME_COMMAND,
            HOMES_COMMAND
    );

    /**
     * Returns the spawn command.
     * @return The spawn command
     */
    public static @NotNull SpawnCommand getSpawnCommand() {
        return SPAWN_COMMAND;
    }

    /**
     * Returns the back command.
     * @return The back command
     */
    public static @NotNull BackCommand getBackCommand() {
        return BACK_COMMAND;
    }

    /**
     * Returns the set spawn command.
     * @return The set spawn command
     */
    public static @NotNull SetSpawnCommand getSetSpawnCommand() {
        return SET_SPAWN_COMMAND;
    }

    /**
     * Returns the home command.
     * @return The home command
     */
    public static @NotNull HomeCommand getHomeCommand() {
        return HOME_COMMAND;
    }

    public static @NotNull SetHomeCommand getSetHomeCommand() {
        return SET_HOME_COMMAND;
    }

    public static @NotNull DeleteHomeCommand getDeleteHomeCommand() {
        return DELETE_HOME_COMMAND;
    }

    public static @NotNull HomesCommand getHomesCommand() {
        return HOMES_COMMAND;
    }

    /**
     * Returns the set of all commands.
     *
     * @return The set of all commands
     */
    public static @NotNull Set<MiraeCommand> getCommands() {
        return ALL_COMMANDS;
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Commands() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
