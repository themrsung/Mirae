package com.themrsung.mirae.account;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.economy.Wallet;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
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

        this.wallet = Wallet.createWallet();
        this.walletFrozen = false;

        this.extraHomeMap = new ConcurrentHashMap<>();
        this.maxExtraHomes = DEFAULT_MAX_HOMES;

        this.skillLevelMap = new ConcurrentHashMap<>();

        this.mailList = Collections.synchronizedList(new ArrayList<>());

        this.ignoredAccountIds = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Creates a new account.
     *
     * @param uniqueId The unique identifier
     * @param name     The name
     * @param wallet   The wallet
     */
    SynchronizedAccount(@NotNull UUID uniqueId, @NotNull String name, @NotNull Wallet wallet) {
        this.uniqueId = uniqueId;
        this.name = name;

        this.tier = AccountTier.DEFAULT;
        this.titleSet = EnumSet.of(AccountTitle.EMPTY);
        this.title = AccountTitle.EMPTY;

        this.wallet = wallet;

        this.extraHomeMap = new ConcurrentHashMap<>();
        this.maxExtraHomes = DEFAULT_MAX_HOMES;

        this.skillLevelMap = new ConcurrentHashMap<>();

        this.mailList = Collections.synchronizedList(new ArrayList<>());

        this.ignoredAccountIds = Collections.synchronizedSet(new HashSet<>());
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
        return MX.isBlank(displayName) ? MX.stylizeText(name) : displayName;
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable Style fallbackStyle) {
        if (fallbackStyle == null) return getDisplayName();
        return MX.isBlank(displayName) ? Component.text(name).style(fallbackStyle) : displayName;
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

    private final @NotNull Wallet wallet;
    private boolean walletFrozen;

    @Override
    public @NotNull Wallet getWallet() {
        return wallet;
    }

    @Override
    public boolean isWalletFrozen() {
        return walletFrozen;
    }

    @Override
    public void setWalletFrozen(boolean frozen) {
        this.walletFrozen = frozen;
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
        skillLevelMap.put(type, level);
    }

    @Override
    public void incrementLevel(@NotNull SkillType type) {
        skillLevelMap.put(type, getSkillLevel(type) + 1);
    }

    @Override
    public void decrementLevel(@NotNull SkillType type) {
        decrementLevel(type, false);
    }

    @Override
    public void decrementLevel(@NotNull SkillType type, boolean allowNegativeLevel) {
        long current = getSkillLevel(type);
        if (current <= 0 && !allowNegativeLevel) return;

        skillLevelMap.put(type, current - 1);
    }

    @Override
    public void clearSkillLevels() {
        skillLevelMap.clear();
    }

    /// Social

    private @NotNull List<Component> mailList;
    private boolean muted;
    private @Nullable LocalDateTime muteExpiration;
    private final @NotNull Set<UUID> ignoredAccountIds;

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
}
