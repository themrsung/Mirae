package com.themrsung.mirae.account;

import com.google.gson.*;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.gson.SkillTypeLongPair;
import com.themrsung.mirae.gson.StringCoordinatePair;
import com.themrsung.mirae.skill.SkillType;
import com.themrsung.mirae.util.Coordinate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * A Mirae account.
 */
public interface Account extends Serializable {
    /**
     * The default number of max homes.
     */
    int DEFAULT_MAX_HOMES = 3;

    /**
     * The default number of max sell listings.
     */
    int DEFAULT_MAX_SELL_LISTINGS = 10;

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
     * Returns the raw display name.
     *
     * @return The raw display name if present, {@code null} otherwise
     */
    @Nullable Component getRawDisplayName();

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

    /// Economy

    /**
     * Returns the balance of this account.
     *
     * @return The balance of this account
     */
    double getBalance();

    /**
     * Returns the coin balance of this account.
     *
     * @return The coin balance of this account
     */
    long getCoinBalance();

    /**
     * Modifies the balance of this account.
     *
     * @param change The net change
     * @return The resulting balance after
     */
    double modifyBalance(double change);

    /**
     * Modifies the balance of this account.
     *
     * @param change The net change
     * @param cause  The cause
     * @return The resulting balance after
     */
    double modifyBalance(double change, @Nullable EconomyCause cause);

    /**
     * Modifies the balance of this account.
     *
     * @param change  The net change
     * @param cause   The cause
     * @param message The message
     * @return The resulting balance after
     */
    double modifyBalance(double change, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Modifies the balance of this account.
     *
     * @param function The modifier function
     * @return The resulting balance after
     */
    double modifyBalance(@NotNull DoubleUnaryOperator function);

    /**
     * Modifies the balance of this account.
     *
     * @param function The modifier function
     * @param cause    The cause
     * @return The resulting balance after
     */
    double modifyBalance(@NotNull DoubleUnaryOperator function, @Nullable EconomyCause cause);

    /**
     * Modifies the balance of this account.
     *
     * @param function The modifier function
     * @param cause    The cause
     * @param message  The message
     * @return The resulting balance after
     */
    double modifyBalance(@NotNull DoubleUnaryOperator function, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Modifies the coin balance of this account.
     *
     * @param change The net change
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(long change);

    /**
     * Modifies the coin balance of this account.
     *
     * @param change The net change
     * @param cause  The cause
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(long change, @Nullable EconomyCause cause);

    /**
     * Modifies the coin balance of this account.
     *
     * @param change  The net change
     * @param cause   The cause
     * @param message The message
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(long change, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Modifies the coin balance of this account.
     *
     * @param function The modifier function.
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(@NotNull LongUnaryOperator function);

    /**
     * Modifies the coin balance of this account.
     *
     * @param function The modifier function.
     * @param cause    The cause
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(@NotNull LongUnaryOperator function, @Nullable EconomyCause cause);

    /**
     * Modifies the coin balance of this account.
     *
     * @param function The modifier function.
     * @param cause    The cause
     * @param message  The message
     * @return The resulting coin balance after
     */
    long modifyCoinBalance(@NotNull LongUnaryOperator function, @Nullable EconomyCause cause, @Nullable String message);

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
    /// Trading
    ///

    /**
     * Returns the maximum number of sell listings allowed.
     *
     * @return The maximum number of sell listings
     */
    int getMaxSellListings();

    /**
     * Sets the maximum number of sell listings allowed
     *
     * @param maxListings The maximum number
     */
    void setMaxSellListings(int maxListings);

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

    /**
     * Returns whether this account is in local chat.
     *
     * @return {@code true} if in local chat
     */
    boolean inLocalChat();

    /**
     * Sets whether this account is in local chat.
     *
     * @param localChat {@code true} for local chat
     */
    void setLocalChat(boolean localChat);

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

    /// Misc.

    /**
     * Returns whether the account is hiding the scoreboard.
     *
     * @return {@code true} if the account is hiding the scoreboard
     */
    boolean hideScoreboard();

    /**
     * Sets whether the account is hiding the scoreboard.
     *
     * @param hideScoreboard {@code true} to hide
     */
    void setHideScoreboard(boolean hideScoreboard);

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

    /// GSON

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    static @NotNull Serializer serializer() {
        return Serializer.SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    static @NotNull Deserializer deserializer() {
        return Deserializer.DESERIALIZER;
    }

    /**
     * Serializer class.
     */
    final class Serializer implements JsonSerializer<Account> {
        private static final @NotNull Serializer SERIALIZER = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(Account account, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            // Identification

            object.add("uniqueId", context.serialize(account.getUniqueId()));
            object.add("name", context.serialize(account.getName()));
            object.add("displayName", account.hasDisplayName() ? GsonComponentSerializer.gson()
                    .serializeToTree(Objects.requireNonNullElse(account.getRawDisplayName(), Component.empty())) : JsonNull.INSTANCE);

            // Tier & Title

            object.add("accountTier", context.serialize(account.getTier()));
            object.add("currentTitle", context.serialize(account.getCurrentTitle()));

            JsonArray ownedTitles = new JsonArray();
            account.getTitleSet().forEach(title -> ownedTitles.add(context.serialize(title)));
            object.add("ownedTitles", ownedTitles);

            // Wallet

            object.add("balance", new JsonPrimitive(account.getBalance()));
            object.add("coinBalance", new JsonPrimitive(account.getCoinBalance()));
            object.add("walletFrozen", new JsonPrimitive(account.isWalletFrozen()));

            // Trading

            object.add("maxSellListings", new JsonPrimitive(account.getMaxSellListings()));

            // Homes

            Coordinate home = account.getHome() != null ? new Coordinate(account.getHome()) : null;
            Map<String, Coordinate> extraHomeMap = new HashMap<>();
            account.getExtraHomeMap().forEach((k, v) -> extraHomeMap.put(k, new Coordinate(v)));

            object.add("home", context.serialize(home));

            JsonArray extraHomes = new JsonArray();
            extraHomeMap.forEach((k, v) -> {
                StringCoordinatePair pair = new StringCoordinatePair(k, v);
                extraHomes.add(context.serialize(pair));
            });

            object.add("extraHomes", extraHomes);
            object.add("maxExtraHomes", new JsonPrimitive(account.getMaxExtraHomes()));

            // Stats

            object.add("lastSeenTime", account.getLastSeenTime() != null ? context.serialize(account.getLastSeenTime()) : JsonNull.INSTANCE);
            object.add("lastSeenLocation", account.getLastSeenLocation() != null ? context.serialize(new Coordinate(account.getLastSeenLocation())) : JsonNull.INSTANCE);

            JsonArray skillLevels = new JsonArray();
            account.getSkillLevelMap().forEach((k, v) -> {
                SkillTypeLongPair pair = new SkillTypeLongPair(k, v);
                skillLevels.add(context.serialize(pair));
            });
            object.add("skillLevels", skillLevels);

            // Social

            JsonArray mailList = new JsonArray();
            account.getMailList().forEach(m -> mailList.add(GsonComponentSerializer.gson().serializeToTree(m)));
            object.add("mailList", mailList);

            object.add("muted", new JsonPrimitive(account.isMuted()));
            object.add("muteExpiration", account.getMuteExpiration() != null ? context.serialize(account.getMuteExpiration()) : JsonNull.INSTANCE);

            JsonArray ignoredAccountIds = new JsonArray();
            account.getIgnoredAccountIds().forEach(id -> ignoredAccountIds.add(context.serialize(id)));
            object.add("ignoredAccountIds", ignoredAccountIds);

            object.add("localChat", new JsonPrimitive(account.inLocalChat()));

            // Misc.

            object.add("hideScoreboard", new JsonPrimitive(account.hideScoreboard()));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    final class Deserializer implements JsonDeserializer<Account> {
        private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

        private Deserializer() {
        }

        @Override
        public Account deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();

            // Identification

            UUID uniqueId = null;
            String name = null;

            if (!object.has("uniqueId") || object.get("uniqueId").isJsonNull()) {
                throw new JsonParseException("Missing or invalid required parameter \"uniqueId\".");
            }

            uniqueId = context.deserialize(object.get("uniqueId"), UUID.class);

            if (!object.has("name") || object.get("name").isJsonNull()) {
                throw new JsonParseException("Missing or invalid required parameter \"name\".");
            }

            name = object.get("name").getAsString();

            Account account = new SynchronizedAccount(uniqueId, name);

            if (object.has("displayName") && !object.get("displayName").isJsonNull()) {
                account.setDisplayName(GsonComponentSerializer.gson().deserializeFromTree(object.get("displayName")));
            }

            // Tier & title

            if (object.has("accountTier") && !object.get("accountTier").isJsonNull()) {
                account.setTier(context.deserialize(object.get("accountTier"), AccountTier.class));
            }

            if (object.has("currentTitle") && !object.get("currentTitle").isJsonNull()) {
                account.setCurrentTitle(context.deserialize(object.get("currentTitle"), AccountTitle.class));
            }

            if (object.has("ownedTitles") && object.get("ownedTitles").isJsonArray()) {
                JsonArray array = object.get("ownedTitles").getAsJsonArray();
                array.forEach(entry -> {
                    AccountTitle title = context.deserialize(entry, AccountTitle.class);
                    account.addTitle(title);
                });
            }

            // Wallet

            if (object.has("balance") && object.get("balance").isJsonPrimitive()) {
                account.modifyBalance(object.get("balance").getAsDouble(), EconomyCause.INITIALIZED);
            } else {
                Mirae.getInstance().getLogger().warning("Parameter \"balance\" not found for account \"" + uniqueId + "\"!");
            }

            if (object.has("coinBalance") && object.get("coinBalance").isJsonPrimitive()) {
                account.modifyCoinBalance(object.get("coinBalance").getAsLong(), EconomyCause.INITIALIZED);
            } else {
                Mirae.getInstance().getLogger().warning("Parameter \"balance\" not found for account \"" + uniqueId + "\"!");
            }

            if (object.has("walletFrozen") && object.get("walletFrozen").isJsonPrimitive()) {
                account.setWalletFrozen(object.get("walletFrozen").getAsBoolean());
            }

            // Trading

            if (object.has("maxSellListings") && object.get("maxSellListings").isJsonPrimitive()) {
                account.setMaxSellListings(object.get("maxSellListings").getAsInt());
            }

            // Homes

            if (object.has("home") && !object.get("home").isJsonNull()) {
                Coordinate c = context.deserialize(object.get("home"), Coordinate.class);
                try {
                    account.setHome(c.asLocation());
                } catch (IllegalArgumentException ignored) {
                }
            }

            if (object.has("extraHomes") && object.get("extraHomes").isJsonArray()) {
                JsonArray extraHomes = object.get("extraHomes").getAsJsonArray();
                extraHomes.forEach(entry -> { // Lambda isolates individual exceptions
                    StringCoordinatePair pair = context.deserialize(entry, StringCoordinatePair.class);
                    try {
                        account.setExtraHome(pair.getKey(), pair.getValue().asLocation());
                    } catch (IllegalArgumentException ignored) {
                    } // Nullify
                });
            }

            if (object.has("maxExtraHomes") && object.get("maxExtraHomes").isJsonPrimitive()) {
                account.setMaxExtraHomes(object.get("maxExtraHomes").getAsInt());
            }

            // Stats

            if (object.has("lastSeenTime") && !object.get("lastSeenTime").isJsonNull()) {
                account.setLastSeenTime(context.deserialize(object.get("lastSeenTime"), LocalDateTime.class));
            }

            if (object.has("lastSeenLocation") && !object.get("lastSeenLocation").isJsonNull()) {
                Coordinate c = context.deserialize(object.get("lastSeenLocation"), Coordinate.class);
                try {
                    account.setLastSeenLocation(c.asLocation());
                } catch (IllegalArgumentException ignored) {
                } // Nullify
            }

            if (object.has("skillLevels") && object.get("skillLevels").isJsonArray()) {
                JsonArray skillLevels = object.get("skillLevels").getAsJsonArray();
                skillLevels.forEach(entry -> {
                    SkillTypeLongPair pair = context.deserialize(entry, SkillTypeLongPair.class);
                    account.setSkillLevel(pair.getKey(), pair.getValue());
                });
            }

            // Social

            if (object.has("mailList") && object.get("mailList").isJsonArray()) {
                JsonArray mailList = object.get("mailList").getAsJsonArray();
                mailList.forEach(m -> {
                    Component mail = GsonComponentSerializer.gson().deserializeFromTree(m);
                    account.addMail(mail);
                });
            }

            if (object.has("muted") && object.get("muted").isJsonPrimitive()) {
                account.setMuted(object.get("muted").getAsBoolean());
            }

            if (object.has("muteExpiration") && !object.get("muteExpiration").isJsonNull()) {
                account.setMuted(account.isMuted(), context.deserialize(object.get("muteExpiration"), LocalDateTime.class));
            }

            if (object.has("ignoredAccountIds") && object.get("ignoredAccountIds").isJsonArray()) {
                JsonArray ignoredAccountIds = object.get("ignoredAccountIds").getAsJsonArray();
                ignoredAccountIds.forEach(id -> account.setIgnoringAccount((UUID) context.deserialize(id, UUID.class), true));
            }

            if (object.has("localChat") && object.get("localChat").isJsonPrimitive()) {
                account.setLocalChat(object.get("localChat").getAsBoolean());
            }

            // Misc.

            if (object.has("hideScoreboard") && object.get("hideScoreboard").isJsonPrimitive()) {
                account.setHideScoreboard(object.get("hideScoreboard").getAsBoolean());
            }

            return account;
        }
    }
}
