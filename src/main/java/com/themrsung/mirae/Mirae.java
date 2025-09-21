package com.themrsung.mirae;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.Commands;
import com.themrsung.mirae.economy.VaultEconomyAdapter;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.state.State;
import com.themrsung.mirae.task.Tasks;
import com.themrsung.mirae.webhook.Webhook;
import me.sjun.exponential.AbstractExpoModule;
import me.sjun.exponential.ExpoBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Main class.
 */
public final class Mirae extends AbstractExpoModule {
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
        return Optional.ofNullable(instance).orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns the plugin instance.
     *
     * @return The Expo plugin instance
     */
    public static @NotNull ExpoBase getPlugin() {
        return getInstance().getExpo();
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
     * @return The webhook
     */
    public static @NotNull Webhook getWebhook() {
        return WEBHOOK;
    }

    @Override
    protected void onRegistration() {
        instance = this;

        ExpoBase plugin = getExpo();
        Logger logger = plugin.getLogger();

        logger.info("Loading Mirae plugin...");

        // Register listeners
        var pm = plugin.getServer().getPluginManager();
        Listeners.getListeners().forEach(listener -> pm.registerEvents(listener, plugin));

        // Register commands
        var cm = plugin.getServer().getCommandMap();
        Commands.getCommands().forEach(cmd -> cm.register("mirae", cmd));

        // Register tasks
        Tasks.registerTasks(plugin, Bukkit.getScheduler());

        // Register economy to Vault
        var sm = plugin.getServer().getServicesManager();
        sm.register(Economy.class, VaultEconomyAdapter.createAdapter(STATE), plugin, ServicePriority.Normal);

        // Load data
        try {
            STATE.load();
        } catch (IOException e) {
            logger.severe("Failed to load data from disk: " + e.getMessage());
        }

        // Update names
        getState().getAccounts().forEach(Account::updateName);

        logger.info("Mirae plugin loaded!");
    }

    @Override
    protected void onUnregistration() {
        ExpoBase plugin = getExpo();
        Logger logger = plugin.getLogger();

        logger.info("Shutting down Mirae plugin...");

        try {
            STATE.save();
        } catch (IOException e) {
            logger.severe("Failed to save data to disk: " + e.getMessage());
        }

        logger.info("Mirae plugin disabled!");

        instance = null;
        expo = null;
    }

    /**
     * Returns the plugin logger.
     *
     * @return The logger
     */
    public @NotNull Logger getLogger() {
        return getExpo().getLogger();
    }

    /**
     * Returns the server instance.
     *
     * @return The server
     */
    public @NotNull Server getServer() {
        return getExpo().getServer();
    }

    /**
     * Returns whether the plugin is enabled.
     *
     * @return {@code true} if the plugin is enabled, {@code false} otherwise
     */
    public boolean isEnabled() {
        return getExpo().isEnabled();
    }
}
