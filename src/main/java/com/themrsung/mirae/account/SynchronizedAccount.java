package com.themrsung.mirae.account;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.event.economy.AccountBalanceModifiedEvent;
import com.themrsung.mirae.event.economy.AccountCoinBalanceModifiedEvent;
import com.themrsung.mirae.event.skill.AccountSkillLevelModifiedEvent;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * Default synchronized implementation of {@link Account}.
 */
public class SynchronizedAccount implements Account {
    /**
     * Creates a new account.
     *
     * @param uniqueId The unique identifier
     */
    SynchronizedAccount(@NotNull UUID uniqueId) {
        this.uniqueId = uniqueId;

        OfflinePlayer player = Bukkit.getOfflinePlayer(uniqueId);
        this.name = Objects.requireNonNullElse(player.getName(), "");

        this.tier = AccountTier.DEFAULT;
        this.titleSet = EnumSet.of(AccountTitle.EMPTY);
        this.title = AccountTitle.EMPTY;

        this.balance = 0;
        this.coinBalance = 0;
        this.walletFrozen = false;

        this.maxSellListings = DEFAULT_MAX_SELL_LISTINGS;

        this.extraHomeMap = new ConcurrentHashMap<>();
        this.maxExtraHomes = DEFAULT_MAX_HOMES;

        this.skillLevelMap = new ConcurrentHashMap<>();

        this.mailList = Collections.synchronizedList(new ArrayList<>());

        this.ignoredAccountIds = Collections.synchronizedSet(new HashSet<>());

        this.hideScoreboard = false;
    }

    /**
     * Creates a new account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     */
    SynchronizedAccount(@NotNull UUID uniqueId, @NotNull String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        this.tier = AccountTier.DEFAULT;
        this.titleSet = EnumSet.of(AccountTitle.EMPTY);
        this.title = AccountTitle.EMPTY;

        this.balance = 0;
        this.coinBalance = 0;
        this.walletFrozen = false;

        this.maxSellListings = DEFAULT_MAX_SELL_LISTINGS;

        this.extraHomeMap = new ConcurrentHashMap<>();
        this.maxExtraHomes = DEFAULT_MAX_HOMES;

        this.skillLevelMap = new ConcurrentHashMap<>();

        this.mailList = Collections.synchronizedList(new ArrayList<>());

        this.ignoredAccountIds = Collections.synchronizedSet(new HashSet<>());

        this.hideScoreboard = false;
    }

    /// Identification

    private final @NotNull UUID uniqueId;
    private @NotNull String name;
    private @Nullable Component displayName;

    @Override
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getDisplayName(MX.STYLE_NORMAL);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable Style fallbackStyle) {
        if (fallbackStyle == null) return getDisplayName();
        Component base = MX.isBlank(displayName) ? Component.text(name).style(fallbackStyle) : displayName;

        Player player = getPlayer();
        if (player == null) return base;

        return Component.empty()

                .append(base.hoverEvent(player.asHoverEvent())
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dm " + name + " ")))

                .append(Component.empty().style(MX.STYLE_NORMAL));
    }

    @Override
    public @Nullable Component getRawDisplayName() {
        return displayName;
    }

    @Override
    public boolean hasDisplayName() {
        return !MX.isBlank(displayName);
    }

    @Override
    public synchronized void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    /// Tier & Title

    private @NotNull AccountTier tier;
    private final @NotNull EnumSet<AccountTitle> titleSet;
    private @NotNull AccountTitle title;

    @Override
    public @NotNull AccountTier getTier() {
        return tier;
    }

    @Override
    public synchronized void setTier(@NotNull AccountTier tier) {
        this.tier = tier;
    }

    @Override
    public @NotNull EnumSet<AccountTitle> getTitleSet() {
        return EnumSet.copyOf(titleSet);
    }

    @Override
    public boolean hasTitle(@Nullable AccountTitle title) {
        return titleSet.contains(title);
    }

    @Override
    public synchronized void addTitle(@NotNull AccountTitle title) {
        titleSet.add(title);
    }

    @Override
    public synchronized void removeTitle(@NotNull AccountTitle title) {
        titleSet.remove(title);
    }

    @Override
    public synchronized void clearTitleSet() {
        titleSet.clear();
    }

    @Override
    public @NotNull AccountTitle getCurrentTitle() {
        return title;
    }

    @Override
    public synchronized void setCurrentTitle(@NotNull AccountTitle title) {
        this.title = title;
    }

    /// Economy

    private double balance;
    private long coinBalance;
    private boolean walletFrozen;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public long getCoinBalance() {
        return coinBalance;
    }

    @Override
    public double modifyBalance(double change) {
        return modifyBalance(change, null, null);
    }

    @Override
    public double modifyBalance(double change, @Nullable EconomyCause cause) {
        return modifyBalance(change, cause, null);
    }

    @Override
    public double modifyBalance(double change, @Nullable EconomyCause cause, @Nullable String message) {
        double balanceBefore = balance;
        balance += change;
        double balanceAfter = balance;

        AccountBalanceModifiedEvent event = AccountBalanceModifiedEvent.builder()
                .account(this)
                .cause(cause)
                .message(message)
                .balanceChange(change)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        Bukkit.getServer().getPluginManager().callEvent(event);

        return balanceAfter;
    }

    @Override
    public double modifyBalance(@NotNull DoubleUnaryOperator function) {
        return modifyBalance(function, null, null);
    }

    @Override
    public double modifyBalance(@NotNull DoubleUnaryOperator function, @Nullable EconomyCause cause) {
        return modifyBalance(function, cause, null);
    }

    @Override
    public double modifyBalance(@NotNull DoubleUnaryOperator function, @Nullable EconomyCause cause, @Nullable String message) {
        return modifyBalance(function.applyAsDouble(balance), cause, message);
    }

    @Override
    public long modifyCoinBalance(long change) {
        return modifyCoinBalance(change, null, null);
    }

    @Override
    public long modifyCoinBalance(long change, @Nullable EconomyCause cause) {
        return modifyCoinBalance(change, cause, null);
    }

    @Override
    public long modifyCoinBalance(long change, @Nullable EconomyCause cause, @Nullable String message) {
        long balanceBefore = coinBalance;
        coinBalance += change;
        long balanceAfter = coinBalance;

        AccountCoinBalanceModifiedEvent event = AccountCoinBalanceModifiedEvent.builder()
                .account(this)
                .cause(cause)
                .message(message)
                .coinBalanceChange(change)
                .coinBalanceBefore(balanceBefore)
                .coinBalanceAfter(balanceAfter)
                .build();

        Bukkit.getServer().getPluginManager().callEvent(event);

        return balanceAfter;
    }

    @Override
    public long modifyCoinBalance(@NotNull LongUnaryOperator function) {
        return modifyCoinBalance(function, null, null);
    }

    @Override
    public long modifyCoinBalance(@NotNull LongUnaryOperator function, @Nullable EconomyCause cause) {
        return modifyCoinBalance(function, cause, null);
    }

    @Override
    public long modifyCoinBalance(@NotNull LongUnaryOperator function, @Nullable EconomyCause cause, @Nullable String message) {
        return modifyCoinBalance(function.applyAsLong(coinBalance), cause, message);
    }

    @Override
    public boolean isWalletFrozen() {
        return walletFrozen;
    }

    @Override
    public void setWalletFrozen(boolean frozen) {
        this.walletFrozen = frozen;
    }

    /// Trading

    private int maxSellListings;

    @Override
    public int getMaxSellListings() {
        return maxSellListings;
    }

    @Override
    public void setMaxSellListings(int maxListings) {
        this.maxSellListings = maxListings;
    }

    /// Homes

    private Location home;
    private final @NotNull Map<String, Location> extraHomeMap;
    private int maxExtraHomes;

    @Override
    public @Nullable Location getHome() {
        return home;
    }

    @Override
    public synchronized void setHome(@Nullable Location home) {
        this.home = home;
    }

    @Override
    public @NotNull Map<String, Location> getExtraHomeMap() {
        return Map.copyOf(extraHomeMap);
    }

    @Override
    public @Nullable Location getExtraHome(@Nullable String key) {
        return extraHomeMap.get(key);
    }

    @Override
    public boolean hasExtraHome(@Nullable String key) {
        return extraHomeMap.containsKey(key);
    }

    @Override
    public boolean setExtraHome(@NotNull String key, @Nullable Location home) {
        if (home == null) {
            return extraHomeMap.remove(key) != null;
        } else {
            return !Objects.equals(home, extraHomeMap.put(key, home));
        }
    }

    @Override
    public int getMaxExtraHomes() {
        return maxExtraHomes;
    }

    @Override
    public synchronized void setMaxExtraHomes(int maxExtraHomes) {
        this.maxExtraHomes = maxExtraHomes;
    }

    @Override
    public void clearExtraHomes() {
        extraHomeMap.clear();
    }

    /// Statistics

    private @Nullable LocalDateTime lastSeenTime;
    private @Nullable Location lastSeenLocation;
    private final @NotNull Map<SkillType, Long> skillLevelMap;

    @Override
    public @Nullable LocalDateTime getLastSeenTime() {
        return lastSeenTime;
    }

    @Override
    public @Nullable Location getLastSeenLocation() {
        return lastSeenLocation;
    }

    @Override
    public synchronized void setLastSeenTime(@Nullable LocalDateTime lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    @Override
    public synchronized void setLastSeenLocation(@Nullable Location lastSeenLocation) {
        this.lastSeenLocation = lastSeenLocation;
    }

    @Override
    public @NotNull Map<SkillType, Long> getSkillLevelMap() {
        return Map.copyOf(skillLevelMap);
    }

    @Override
    public long getSkillLevel(@Nullable SkillType type) {
        return skillLevelMap.getOrDefault(type, 0L);
    }

    @Override
    public boolean hasSkillLevel(@Nullable SkillType type, long minimumLevel) {
        return getSkillLevel(type) >= minimumLevel;
    }

    @Override
    public void setSkillLevel(@NotNull SkillType type, long level) {
        long levelBefore = getSkillLevel(type);
        skillLevelMap.put(type, level);

        Bukkit.getPluginManager().callEvent(new AccountSkillLevelModifiedEvent(this, type, levelBefore, level));
    }

    @Override
    public void incrementLevel(@NotNull SkillType type) {
        long levelBefore = getSkillLevel(type);
        long levelAfter = levelBefore + 1;
        skillLevelMap.put(type, levelAfter);

        Bukkit.getPluginManager().callEvent(new AccountSkillLevelModifiedEvent(this, type, levelBefore, levelAfter));
    }

    @Override
    public void decrementLevel(@NotNull SkillType type) {
        decrementLevel(type, false);
    }

    @Override
    public void decrementLevel(@NotNull SkillType type, boolean allowNegativeLevel) {
        long levelBefore = getSkillLevel(type);
        if (levelBefore <= 0 && !allowNegativeLevel) return;

        long levelAfter = levelBefore - 1;
        skillLevelMap.put(type, levelAfter);

        Bukkit.getPluginManager().callEvent(new AccountSkillLevelModifiedEvent(this, type, levelBefore, levelAfter));
    }

    @Override
    public void clearSkillLevels() {
        skillLevelMap.forEach((k, v) -> {
            if (v != 0) Bukkit.getPluginManager().callEvent(new AccountSkillLevelModifiedEvent(this, k, v, 0));
        });

        skillLevelMap.clear();
    }

    /// Social

    private final @NotNull List<Component> mailList;
    private boolean muted;
    private @Nullable LocalDateTime muteExpiration;
    private final @NotNull Set<UUID> ignoredAccountIds;
    private boolean localChat;

    @Override
    public @NotNull List<Component> getMailList() {
        return List.copyOf(mailList);
    }

    @Override
    public void addMail(@NotNull Component mail) {
        mailList.add(mail);
    }

    @Override
    public void clearMailList(int cutoff) {
        if (mailList.size() <= cutoff) {
            return;
        }

        mailList.subList(0, mailList.size() - cutoff).clear();
    }

    @Override
    public void clearMailList() {
        mailList.clear();
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    @Override
    public @Nullable LocalDateTime getMuteExpiration() {
        return muteExpiration;
    }

    @Override
    public synchronized void setMuted(boolean muted) {
        this.muted = muted;
        if (!muted) muteExpiration = null;
    }

    @Override
    public synchronized void setMuted(boolean muted, @Nullable LocalDateTime expiration) {
        this.muted = muted;
        this.muteExpiration = muted ? expiration : null;
    }

    @Override
    public @NotNull Set<UUID> getIgnoredAccountIds() {
        return Set.copyOf(ignoredAccountIds);
    }

    @Override
    public boolean isIgnoringAccount(@Nullable UUID uniqueId) {
        return ignoredAccountIds.contains(uniqueId);
    }

    @Override
    public boolean isIgnoringAccount(@Nullable Account account) {
        return account != null && ignoredAccountIds.contains(account.getUniqueId());
    }

    @Override
    public boolean isIgnoringAccount(@Nullable OfflinePlayer player) {
        return player != null && ignoredAccountIds.contains(player.getUniqueId());
    }

    @Override
    public synchronized boolean ignoreAccount(@Nullable UUID uniqueId) {
        return uniqueId != null && ignoredAccountIds.add(uniqueId);
    }

    @Override
    public boolean ignoreAccount(@Nullable Account account) {
        return account != null && ignoreAccount(account.getUniqueId());
    }

    @Override
    public boolean ignoreAccount(@Nullable OfflinePlayer player) {
        return player != null && ignoreAccount(player.getUniqueId());
    }

    @Override
    public synchronized boolean unignoreAccount(@Nullable UUID uniqueId) {
        return ignoredAccountIds.remove(uniqueId);
    }

    @Override
    public boolean unignoreAccount(@Nullable Account account) {
        return account != null && unignoreAccount(account.getUniqueId());
    }

    @Override
    public boolean unignoreAccount(@Nullable OfflinePlayer player) {
        return player != null && unignoreAccount(player.getUniqueId());
    }

    @Override
    public boolean setIgnoringAccount(@Nullable UUID uniqueId, boolean ignoring) {
        if (ignoring) {
            return ignoreAccount(uniqueId);
        } else {
            return unignoreAccount(uniqueId);
        }
    }

    @Override
    public boolean setIgnoringAccount(@Nullable Account account, boolean ignoring) {
        if (ignoring) {
            return ignoreAccount(account);
        } else {
            return unignoreAccount(account);
        }
    }

    @Override
    public boolean setIgnoringAccount(@Nullable OfflinePlayer player, boolean ignoring) {
        if (ignoring) {
            return ignoreAccount(player);
        } else {
            return ignoreAccount(player);
        }
    }

    @Override
    public boolean inLocalChat() {
        return localChat;
    }

    @Override
    public void setLocalChat(boolean localChat) {
        this.localChat = localChat;
    }

    @Override
    public synchronized void clearIgnoredAccounts() {
        ignoredAccountIds.clear();
    }

    /// Transient

    private transient @Nullable Location recentTeleportDeparture;
    private transient @Nullable Location recentDeathLocation;

    @Override
    public @Nullable Location getRecentTeleportDeparture() {
        return recentTeleportDeparture;
    }

    @Override
    public synchronized void setRecentTeleportDeparture(@Nullable Location recentTeleportDeparture) {
        this.recentTeleportDeparture = recentTeleportDeparture;
    }

    @Override
    public @Nullable Location getRecentDeathLocation() {
        return recentDeathLocation;
    }

    @Override
    public void setRecentDeathLocation(@Nullable Location recentDeathLocation) {
        this.recentDeathLocation = recentDeathLocation;
    }

    /// Utilities

    @Override
    public void sendMessage(@NotNull Component message) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null && player.isOnline()) {
            player.sendMessage(message);
        } else {
            addMail(message);
        }
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    @Override
    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    @Override
    public void updateName() {
        var player = Bukkit.getOfflinePlayer(uniqueId);

        if (player.getName() != null) {
            name = player.getName();
        } else {
            name = "";
        }
    }

    /// Misc.

    private boolean hideScoreboard;

    @Override
    public boolean hideScoreboard() {
        return hideScoreboard;
    }

    @Override
    public void setHideScoreboard(boolean hideScoreboard) {
        this.hideScoreboard = hideScoreboard;
    }
}
