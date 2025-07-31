package com.themrsung.mirae.account;

import com.themrsung.mirae.economy.Wallet;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A Mirae account.
 */
public interface Account extends Serializable {
    /**
     * The default number of max homes.
     */
    int DEFAULT_MAX_HOMES = 3;

    ///
    /// Factory
    ///

    /**
     * Creates a new account.
     *
     * @param uniqueId The unique identifier of the account
     * @return The account
     */
    static @NotNull Account createAccount(@NotNull UUID uniqueId) {
        return new SynchronizedAccount(uniqueId);
    }

    ///
    /// Identification
    ///

    /**
     * Returns the unique identifier.
     *
     * @return The unique identifier
     */
    @NotNull UUID getUniqueId();

    /**
     * Returns the name.
     *
     * @return The name
     */
    @NotNull String getName();

    /**
     * Returns the display name.
     *
     * @return The display name if present, a default-styled name otherwise
     */
    @NotNull Component getDisplayName();

    /**
     * Returns the display name.
     *
     * @param fallbackStyle The fallback style to apply if there is no display name
     * @return The display name if present, the styled name otherwise
     */
    @NotNull Component getDisplayName(@Nullable Style fallbackStyle);

    /**
     * Returns whether this account has a display name.
     *
     * @return {@code true} if it has a display name
     */
    boolean hasDisplayName();

    /**
     * Sets the display name.
     *
     * @param displayName The display name to set
     */
    void setDisplayName(@Nullable Component displayName);

    ///
    /// Tier & Title
    ///

    /**
     * Returns the account tier.
     *
     * @return The account tier
     */
    @NotNull AccountTier getTier();

    /**
     * Sets the account tier.
     *
     * @param tier The account tier
     */
    void setTier(@NotNull AccountTier tier);

    /**
     * Returns the set of titles.
     *
     * @return The set of titles
     */
    @NotNull EnumSet<AccountTitle> getTitleSet();

    /**
     * Returns whether the account has the title.
     *
     * @param title The title
     * @return {@code true} if the account has the title
     */
    boolean hasTitle(@Nullable AccountTitle title);

    /**
     * Adds the title to this account.
     *
     * @param title The title to add
     */
    void addTitle(@NotNull AccountTitle title);

    /**
     * Removes the title from this account.
     *
     * @param title The title to remove
     */
    void removeTitle(@NotNull AccountTitle title);

    /**
     * Clears the set of titles.
     */
    void clearTitleSet();

    /**
     * Returns the account title.
     *
     * @return The account title
     */
    @NotNull AccountTitle getCurrentTitle();

    /**
     * Sets the account title.
     *
     * @param title The account title
     */
    void setCurrentTitle(@NotNull AccountTitle title);

    ///
    /// Economy
    ///

    /**
     * Returns the wallet of this account.
     *
     * @return The wallet
     */
    @NotNull Wallet getWallet();

    /**
     * Returns whether the wallet is frozen.
     *
     * @return {@code true} if frozen
     */
    boolean isWalletFrozen();

    /**
     * Sets whether the wallet is frozen.
     *
     * @param frozen {@code true} if frozen
     */
    void setWalletFrozen(boolean frozen);

    ///
    /// Homes
    ///

    /**
     * Returns the home of this account.
     *
     * @return The home of this account
     */
    @Nullable Location getHome();

    /**
     * Sets the home of this account.
     *
     * @param home The home of this account
     */
    void setHome(@Nullable Location home);

    /**
     * Returns an unmodifiable map of extra homes.
     *
     * @return An unmodifiable map
     */
    @NotNull Map<String, Location> getExtraHomeMap();

    /**
     * Returns the extra home.
     *
     * @param key The key
     * @return The home if present, {@code null} otherwise
     */
    @Nullable Location getExtraHome(@Nullable String key);

    /**
     * Returns whether there is an extra home mapped with the given key.
     *
     * @param key The key to check
     * @return {@code true} if there is an extra home
     */
    boolean hasExtraHome(@Nullable String key);

    /**
     * Sets the extra home.
     *
     * @param key  The key
     * @param home The home to set to, or {@code null}
     * @return {@code true} if and only if the home map was changed
     */
    boolean setExtraHome(@NotNull String key, @Nullable Location home);

    /**
     * Returns the maximum number of extra homes this account has.
     *
     * @return The maximum number of extra homes
     */
    int getMaxExtraHomes();

    /**
     * Sets the number of maximum extra homes.
     *
     * @param homes The maximum number of extra homes
     */
    void setMaxExtraHomes(int homes);

    /**
     * Clears the map of extra homes.
     */
    void clearExtraHomes();

    ///
    /// Statistics
    ///

    /**
     * Returns the last seen time.
     *
     * @return The last seen time
     */
    @Nullable LocalDateTime getLastSeenTime();

    /**
     * Returns the last seen location if present.
     *
     * @return The last seen location if present, {@code null} otherwise
     */
    @Nullable Location getLastSeenLocation();

    /**
     * Sets the last seen time.
     *
     * @param time The last seen time
     */
    void setLastSeenTime(@Nullable LocalDateTime time);

    /**
     * Sets the last seen location.
     *
     * @param seen The last seen location
     */
    void setLastSeenLocation(@Nullable Location seen);

    /**
     * Returns an unmodifiable map of skill levels.
     *
     * @return An unmodifiable map of skill levels
     */
    @NotNull Map<SkillType, Long> getSkillLevelMap();

    /**
     * Returns the skill level.
     *
     * @param type The type
     * @return The level if found, {@code 0} otherwise
     */
    long getSkillLevel(@Nullable SkillType type);

    /**
     * Checks if this account has at least the given skill level.
     *
     * @param type         The type to check
     * @param minimumLevel The minimum level
     * @return {@code true} if this account has the level
     */
    boolean hasSkillLevel(@Nullable SkillType type, long minimumLevel);

    /**
     * Sets the skill level.
     *
     * @param type  The type of skill
     * @param level The level
     */
    void setSkillLevel(@NotNull SkillType type, long level);

    /**
     * Increments the given skill level.
     *
     * @param type The skill type
     */
    void incrementLevel(@NotNull SkillType type);

    /**
     * Decrements the given skill level. Does not allow negative skill level by default.
     *
     * @param type The skill type
     * @see #decrementLevel(SkillType, boolean)
     */
    void decrementLevel(@NotNull SkillType type);

    /**
     * Decrements the given skill level.
     *
     * @param type               The skill level
     * @param allowNegativeLevel Whether negative skill levels are allowed
     */
    void decrementLevel(@NotNull SkillType type, boolean allowNegativeLevel);

    /**
     * Resets every skill level to zero.
     */
    void clearSkillLevels();

    ///
    /// Social
    ///

    /**
     * Returns the list of mails.
     *
     * @return The list of mails
     */
    @NotNull List<Component> getMailList();

    /**
     * Adds mail to the mailbox.
     *
     * @param mail The mail to add
     */
    void addMail(@NotNull Component mail);

    /**
     * Clears the mail list, but retains mail received before the cutoff amount.
     *
     * @param cutoff The number of mails to retain
     */
    void clearMailList(int cutoff);

    /**
     * Clears the mail list.
     */
    void clearMailList();

    /**
     * Returns whether this account has been muted.
     *
     * @return {@code true} if it is muted
     */
    boolean isMuted();

    /**
     * Returns the mute expiration time.
     *
     * @return The mute expiration time
     */
    @Nullable LocalDateTime getMuteExpiration();

    /**
     * Sets whether this account has been muted.
     *
     * @param muted {@code true} if it is muted
     */
    void setMuted(boolean muted);

    /**
     * Sets whether this account has been muted.
     *
     * @param muted      {@code true} if it is muted
     * @param expiration The expiry of the mute
     */
    void setMuted(boolean muted, @Nullable LocalDateTime expiration);

    /**
     * Returns the set of ignored account identifiers.
     *
     * @return The set of ignored account identifiers
     */
    @NotNull Set<UUID> getIgnoredAccountIds();

    /**
     * Returns whether this account is ignoring the given unique identifier.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if ignoring
     */
    boolean isIgnoringAccount(@Nullable UUID uniqueId);

    /**
     * Returns whether this account is ignoring the given account.
     *
     * @param account The account
     * @return {@code true} if ignoring
     */
    boolean isIgnoringAccount(@Nullable Account account);

    /**
     * Returns whether this account is ignoring the given player.
     *
     * @param player The player
     * @return {@code true} if ignoring
     */
    boolean isIgnoringAccount(@Nullable OfflinePlayer player);

    /**
     * Ignores the given account identifier.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if there was a state change
     */
    boolean ignoreAccount(@Nullable UUID uniqueId);

    /**
     * Ignores the given account.
     *
     * @param account The account
     * @return {@code true} if there was a state change
     */
    boolean ignoreAccount(@Nullable Account account);

    /**
     * Ignores the given player.
     *
     * @param player The player
     * @return {@code true} if there was a state change
     */
    boolean ignoreAccount(@Nullable OfflinePlayer player);

    /**
     * Unignores the given account identifier.
     *
     * @param uniqueId The unique identifier
     * @return {@code true} if there was a state change
     */
    boolean unignoreAccount(@Nullable UUID uniqueId);

    /**
     * Unignores the given account.
     *
     * @param account The account
     * @return {@code true} if there was a state change
     */
    boolean unignoreAccount(@Nullable Account account);

    /**
     * Unignores the given player.
     *
     * @param player The player
     * @return {@code true} if there was a state change
     */
    boolean unignoreAccount(@Nullable OfflinePlayer player);

    /**
     * Sets the ignoring status.
     *
     * @param uniqueId The unique identifier
     * @param ignoring Whether it is ignored
     * @return {@code true} if there was a state change
     */
    boolean setIgnoringAccount(@Nullable UUID uniqueId, boolean ignoring);

    /**
     * Sets the ignoring status.
     *
     * @param account  The account
     * @param ignoring Whether it is ignored
     * @return {@code true} if there was a state change
     */
    boolean setIgnoringAccount(@Nullable Account account, boolean ignoring);

    /**
     * Sets the ignoring status.
     *
     * @param player   The player
     * @param ignoring Whether it is ignored
     * @return {@code true} if there was a state change
     */
    boolean setIgnoringAccount(@Nullable OfflinePlayer player, boolean ignoring);

    /**
     * Unignores all accounts.
     */
    void clearIgnoredAccounts();

    ///
    /// Transient Getters / Setters
    ///

    /**
     * Returns the recent teleportation departure.
     *
     * @return The recent teleportation departure
     */
    @Nullable Location getRecentTeleportDeparture();

    /**
     * Sets the last teleport departure.
     *
     * @param location The last teleport departure
     */
    void setRecentTeleportDeparture(@Nullable Location location);

    /**
     * Returns the recent death location.
     *
     * @return The recent death location
     */
    @Nullable Location getRecentDeathLocation();

    /**
     * Sets the recent death location.
     *
     * @param location The location
     */
    void setRecentDeathLocation(@Nullable Location location);

    ///
    /// Utilities
    ///

    /**
     * Sends a content to this account. If offline, it will be saved to the mail list.
     *
     * @param message The content
     */
    void sendMessage(@NotNull Component message);

    /**
     * Returns the offline player.
     *
     * @return The offline player
     */
    @NotNull OfflinePlayer getOfflinePlayer();

    /**
     * Returns the online player.
     *
     * @return The player if available, {@code null} otherwise
     */
    @Nullable Player getPlayer();

    /**
     * Updates the name of the player from the Bukkit servers.
     */
    void updateName();
}
