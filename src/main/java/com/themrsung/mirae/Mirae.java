package com.themrsung.mirae;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.Commands;
import com.themrsung.mirae.economy.VaultEconomyAdapter;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.state.State;
import com.themrsung.mirae.task.Tasks;
import com.themrsung.mirae.webhook.Webhook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Main class.
 */
public final class Mirae extends JavaPlugin {
    /**
     * The state instance.
     */
    private static final @NotNull State STATE = State.empty();

    /**
     * The webhook.
     */
    private static final @NotNull Webhook WEBHOOK = new Webhook();

    /**
     * The plugin instance.
     */
    private static Mirae instance;

    /**
     * Returns the plugin instance.
     *
     * @return The plugin instance
     */
    public static @NotNull Mirae getInstance() { // This is non-null if plugin is enabled.
        return instance;
    }

    /**
     * Returns the economy.
     *
     * @return The economy
     */
    public static @NotNull State getState() {
        return STATE;
    }

    /**
     * Returns the webhook.
     *
     * @return The webhook
     */
    public static @NotNull Webhook getWebhook() {
        return WEBHOOK;
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading Mirae plugin...");

        // Reference instance.
        instance = this;

        // Register listeners
        var pm = getServer().getPluginManager();
        Listeners.getListeners().forEach(listener -> pm.registerEvents(listener, this));

        // Register commands
        var cm = getServer().getCommandMap();
        Commands.getCommands().forEach(cmd -> cm.register("mirae", cmd));

        // Register tasks
        Tasks.registerTasks(this, Bukkit.getScheduler());

        // Register economy to Vault
        var sm = getServer().getServicesManager();
        sm.register(Economy.class, VaultEconomyAdapter.createAdapter(STATE), this, ServicePriority.Normal);

        // Load data
        try {
            STATE.load();
        } catch (IOException e) {
            getLogger().severe("Failed to load data from disk: " + e.getMessage());
        }

        // Update names
        getState().getAccounts().forEach(Account::updateName);

        getLogger().info("Mirae plugin loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down Mirae plugin...");

        try {
            STATE.save();
        } catch (IOException e) {
            getLogger().severe("Failed to save data to disk: " + e.getMessage());
        }

        getLogger().info("Mirae plugin disabled!");
    }
}
