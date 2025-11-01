package com.themrsung.mirae.state;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.social.DirectMessage;
import com.themrsung.mirae.social.TeleportRequest;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A Mirae economy.
 */
public interface State {
    /**
     * The number of minutes until being AFK.
     */
    long AFK_MINUTES = 10;

    /**
     * Creates and returns a new economy instance.
     *
     * @return The new economy instance
     */
    static @NotNull State empty() {
        return new SynchronizedState();
    }

    ///
    /// Warps
    ///

    /**
     * Returns the spawn point.
     *
     * @return The spawn point
     */
    @Nullable Location getSpawnPoint();

    /**
     * Sets the spawn point.
     *
     * @param spawnPoint The spawn point
     */
    void setSpawnPoint(@Nullable Location spawnPoint);

    /**
     * Returns the warp map.
     *
     * @return The warp map
     */
    @NotNull Map<String, Location> getWarpMap();

    /**
     * Returns the warp of matching key.
     *
     * @param key The key
     * @return The warp location
     */
    @Nullable Location getWarp(@Nullable String key);

    /**
     * Returns whether there is a warp.
     *
     * @param key The key
     * @return {@code true} if there is a warp
     */
    boolean hasWarp(@Nullable String key);

    /**
     * Sets the warp location.
     *
     * @param key   The warp key
     * @param value The warp location
     */
    void setWarp(@NotNull String key, @NotNull Location value);

    /**
     * Removes the warp location.
     *
     * @param key The warp key
     */
    void removeWarp(@NotNull String key);

    /**
     * Clears the warp map.
     */
    void clearWarps();

    ///
    /// Accounts
    ///

    /**
     * Returns an unmodifiable map of the accounts.
     *
     * @return An unmodifiable map of the accounts
     */
    @NotNull Map<UUID, Account> getAccountMap();

    /**
     * Returns the list of accounts.
     *
     * @return The list of accounts
     */
    @NotNull List<Account> getAccounts();

    /**
     * Returns the account with the matching unique identifier.
     *
     * @param uniqueId The unique identifier
     * @return The account if found, {@code null} otherwise
     */
    @Nullable Account getAccount(@Nullable UUID uniqueId);

    /**
     * Returns the account of matching player.
     *
     * @param player The player
     * @return The account if found, {@code null} otherwise
     */
    @Nullable Account getAccount(@Nullable OfflinePlayer player);

    /**
     * Returns whether there is an account with the given unique identifier.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if there is an account
     */
    boolean hasAccount(@Nullable UUID uniqueId);

    /**
     * Returns whether there is an account with the matching player.
     *
     * @param player The player
     * @return {@code true} if there is an account
     */
    boolean hasAccount(@Nullable OfflinePlayer player);

    /**
     * Returns whether the account exists.
     *
     * @param account The account
     * @return {@code true} if it exists
     */
    boolean hasAccount(@Nullable Account account);

    /**
     * Adds an account to the economy.
     *
     * @param account The account to add
     * @return {@code true} if the account was added
     */
    boolean addAccount(@NotNull Account account);

    /**
     * Removes an account from this economy.
     *
     * @param account The account to remove
     * @return {@code true} if there was a state change
     */
    boolean removeAccount(@NotNull Account account);

    /**
     * Removes all accounts matching the filter.
     *
     * @param filter The filter to apply
     * @return The number of removed accounts
     */
    int removeAccounts(@NotNull Predicate<? super Account> filter);

    /**
     * Clears the account map.
     */
    void clearAccounts();

    ///
    /// Quests
    ///

    /**
     * Returns the date when the current daily quest chest was generated.
     *
     * @return The quest generation date
     */
    @Nullable LocalDate getDailyQuestDate();

    /**
     * Sets the date when the current daily quest chest was generated.
     *
     * @param date The quest generation date
     */
    void setDailyQuestDate(@Nullable LocalDate date);

    /**
     * Returns the location of the current daily quest chest.
     *
     * @return The quest chest location
     */
    @Nullable Location getDailyQuestLocation();

    /**
     * Sets the location of the current daily quest chest.
     *
     * @param location The quest chest location
     */
    void setDailyQuestLocation(@Nullable Location location);

    ///
    /// Markets
    ///

    /**
     * Returns the copied map of markets.
     *
     * @return The copied map of markets
     */
    @NotNull Map<UUID, Market> getMarketMap();

    /**
     * Returns the list of markets.
     *
     * @return The list of markets
     */
    @NotNull List<Market> getMarkets();

    /**
     * Returns the market with the matching unique identifier.
     *
     * @param uniqueId The unique identifier
     * @return The market if present, {@code null} otherwise
     */
    @Nullable Market getMarket(@Nullable UUID uniqueId);

    /**
     * Returns whether this state has the given market.
     *
     * @param market The market to check
     * @return {@code true} if the market is present
     */
    boolean hasMarket(@Nullable Market market);

    /**
     * Adds a market to this state.
     *
     * @param market The market to add
     * @return {@code true} if the state was changed
     */
    boolean addMarket(@NotNull Market market);

    /**
     * Removes a market from this state.
     *
     * @param market The market to remove
     * @return {@code true} if the state was changed
     */
    boolean removeMarket(@NotNull Market market);

    /**
     * Clears the market map.
     */
    void clearMarkets();

    ///
    /// Freezing
    ///

    /**
     * Returns whether the economy is frozen.
     *
     * @return {@code true} if it is frozen
     */
    boolean isEconomyFrozen();

    /**
     * Sets whether the economy is frozen.
     *
     * @param frozen {@code true} if it is frozen
     */
    void setEconomyFrozen(boolean frozen);

    ///
    /// Withdrawable Balance
    ///

    /**
     * Returns the withdrawable balance.
     *
     * @param uniqueId The account's unique identifier
     * @return The withdrawable balance
     */
    double getWithdrawableBalance(@Nullable UUID uniqueId);

    /**
     * Returns the withdrawable balance.
     *
     * @param player The player
     * @return The withdrawable balance
     */
    double getWithdrawableBalance(@Nullable OfflinePlayer player);

    /**
     * Returns the withdrawable balance.
     *
     * @param account The account
     * @return The withdrawable balance
     */
    double getWithdrawableBalance(@Nullable Account account);

    /**
     * Returns the withdrawable coin balance.
     *
     * @param uniqueId The account's unique identifier
     * @return The withdrawable coin balance
     */
    long getWithdrawableCoinBalance(@Nullable UUID uniqueId);

    /**
     * Returns the withdrawable coin balance.
     *
     * @param player The player
     * @return The withdrawable coin balance
     */
    long getWithdrawableCoinBalance(@Nullable OfflinePlayer player);

    /**
     * Returns the withdrawable coin balance.
     *
     * @param account The account
     * @return The withdrawable coin balance
     */
    long getWithdrawableCoinBalance(@Nullable Account account);

    ///
    /// Actions
    ///

    /**
     * Deposits balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @return The result
     */
    @NotNull EconomyResult depositBalance(@NotNull Account account, double amount);

    /**
     * Deposits balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @return The result
     */
    @NotNull EconomyResult depositBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause);

    /**
     * Deposits balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @param message The content
     * @return The result
     */
    @NotNull EconomyResult depositBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @return The result
     */
    @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @param cause     The cause
     * @return The result
     */
    @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount, @Nullable EconomyCause cause);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @param cause     The cause
     * @param message   The content
     * @return The result
     */
    @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @return The result
     */
    @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @return The result
     */
    @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @param message The content
     * @return The result
     */
    @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Deposits coin balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @return The result
     */
    @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount);

    /**
     * Deposits coin balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @return The result
     */
    @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause);

    /**
     * Deposits coin balance to the account.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @param message The content
     * @return The result
     */
    @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @return The result
     */
    @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @param cause     The cause
     * @return The result
     */
    @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount, @Nullable EconomyCause cause);

    /**
     * Transfers balance.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param amount    The amount
     * @param cause     The cause
     * @param message   The content
     * @return The result
     */
    @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @return The result
     */
    @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @return The result
     */
    @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause);

    /**
     * Withdraws balance.
     *
     * @param account The account
     * @param amount  The amount
     * @param cause   The cause
     * @param message The content
     * @return The result
     */
    @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause, @Nullable String message);

    ///
    /// Transient
    ///

    /**
     * Returns the list of direct messages.
     *
     * @return The list of direct messages
     */
    @NotNull List<DirectMessage> getDirectMessages();

    /**
     * Adds a direct content to the state.
     *
     * @param message The content to add
     */
    void addDirectMessage(@NotNull DirectMessage message);

    /**
     * Clears the list of direct messages, but retains those after the cutoff.
     *
     * @param cutoff The cutoff
     */
    void clearDirectMessages(@NotNull LocalDateTime cutoff);

    /**
     * Clears direct messages.
     */
    void clearDirectMessages();

    /**
     * Returns the list of teleport requests.
     *
     * @return The list of teleport requests
     */
    @NotNull List<TeleportRequest> getTeleportRequests();

    /**
     * Adds a teleport request.
     *
     * @param request The request
     */
    void addTeleportRequest(@NotNull TeleportRequest request);

    /**
     * Removes the teleport request.
     *
     * @param request The request
     */
    void removeTeleportRequest(@NotNull TeleportRequest request);

    /**
     * Clears the list of teleport requests, but retains those after the cutoff.
     *
     * @param cutoff The cutoff
     */
    void clearTeleportRequests(@NotNull LocalDateTime cutoff);

    /**
     * Clears the list of teleport requests.
     */
    void clearTeleportRequests();

    /**
     * Returns the set of AFK accounts.
     *
     * @return The set of AFK accounts
     */
    @NotNull Set<Account> getAfkAccounts();

    /**
     * Returns whether the account if AFK.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if AFK
     */
    boolean isAfk(@Nullable UUID uniqueId);

    /**
     * Returns whether the account is AFK.
     *
     * @param player The player
     * @return {@code true} if AFK
     */
    boolean isAfk(@Nullable OfflinePlayer player);

    /**
     * Returns whether the account is AFK.
     *
     * @param account The account
     * @return {@code true} if AFK
     */
    boolean isAfk(@Nullable Account account);

    /**
     * Logs that the account has performed an activity.
     *
     * @param account The account
     */
    void logAccountActivity(@NotNull Account account);

    /**
     * Clears the activity log data.
     */
    void clearAfkLogs();

    ///
    /// Statistics
    ///

    /**
     * Returns the total issuance of tracked banknotes.
     *
     * @return The tracked banknote issuance
     */
    double getTrackedBanknoteIssuance();

    /**
     * Adjusts the tracked banknote issuance by the specified amount.
     *
     * @param amount The amount to adjust by
     */
    void adjustTrackedBanknoteIssuance(double amount);

    /**
     * Sets the tracked banknote issuance amount.
     *
     * @param amount The new tracked banknote issuance
     */
    void setTrackedBanknoteIssuance(double amount);

    /**
     * Returns the total money supply.
     *
     * @return The total money supply
     */
    double getMoneySupply();

    /**
     * Returns the total coin supply.
     *
     * @return The total coin supply
     */
    long getCoinSupply();

    ///
    /// Input/Output
    ///

    /**
     * Clears transient variables.
     */
    void clearTransient();

    /**
     * Clears all variables.
     */
    void clearAll();

    /**
     * Saves the economy's state to disk.
     *
     * @throws IOException When I/O fails
     */
    void save() throws IOException;

    /**
     * Loads the economy's state from disk.
     *
     * @throws IOException When I/O fails
     */
    void load() throws IOException;
}
