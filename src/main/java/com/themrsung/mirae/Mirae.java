package com.themrsung.mirae;

import com.themrsung.mirae.command.Commands;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.state.MiraeState;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Mirae extends JavaPlugin {
    private static final @NotNull MiraeState STATE = MiraeState.empty();

    /**
     * Returns the plugin instance.
     *
     * @return The plugin instance
     */
    public static @NotNull Mirae getInstance() {
        return (Mirae) Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Mirae"));
    }

    /**
     * Returns the economy.
     *
     * @return The economy
     */
    public static @NotNull MiraeState getState() {
        return STATE;
    }

    @Override
    public void onEnable() {

        // Register listeners
        var pm = getServer().getPluginManager();
        Listeners.getListeners().forEach(listener -> pm.registerEvents(listener, this));

        // Register commands
        var cm = getServer().getCommandMap();
        Commands.getCommands().forEach(cmd -> cm.register("mirae", cmd));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
