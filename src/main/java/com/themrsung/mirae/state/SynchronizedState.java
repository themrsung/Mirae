package com.themrsung.mirae.state;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.economy.Wallet;
import com.themrsung.mirae.event.economy.EconomyCause;
import com.themrsung.mirae.social.DirectMessage;
import com.themrsung.mirae.social.TeleportRequest;
import com.themrsung.mirae.util.MutableIncrement;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Default synchronized implementation of {@link MiraeState}.
 */
public class SynchronizedState implements MiraeState {
    /**
     * Creates an empty state.
     */
    SynchronizedState() {
        // Non-transient
        this.spawnPoint = null;
        this.accountMap = new ConcurrentHashMap<>();

        // Transient
        this.directMessages = Collections.synchronizedList(new ArrayList<>());
        this.teleportRequests = Collections.synchronizedList(new ArrayList<>());
        this.lastActivityTimeMap = new ConcurrentHashMap<>();
    }

    /// Spawn Point

    private @Nullable Location spawnPoint;

    @Override
    public @Nullable Location getSpawnPoint() {
        return spawnPoint;
    }

    @Override
    public synchronized void setSpawnPoint(@Nullable Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    /// Accounts

    private final @NotNull Map<UUID, Account> accountMap;

    @Override
    public @NotNull Map<UUID, Account> getAccountMap() {
        return Map.copyOf(accountMap);
    }

    @Override
    public @NotNull List<Account> getAccounts() {
        return List.copyOf(accountMap.values());
    }

    @Override
    public @Nullable Account getAccount(@Nullable UUID uniqueId) {
        return accountMap.get(uniqueId);
    }

    @Override
    public @Nullable Account getAccount(@Nullable OfflinePlayer player) {
        return player != null ? getAccount(player.getUniqueId()) : null;
    }

    @Override
    public boolean hasAccount(@Nullable UUID uniqueId) {
        return uniqueId != null && accountMap.containsKey(uniqueId);
    }

    @Override
    public boolean hasAccount(@Nullable OfflinePlayer player) {
        return player != null && hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(@Nullable Account account) {
        return account != null && accountMap.containsValue(account);
    }

    @Override
    public boolean addAccount(@NotNull Account account) {
        UUID uniqueId = account.getUniqueId();
        if (accountMap.containsKey(uniqueId)) return false;

        accountMap.put(uniqueId, account);
        logAccountActivity(account);
        return true;
    }

    @Override
    public boolean removeAccount(@NotNull Account account) {
        UUID uniqueId = account.getUniqueId();
        lastActivityTimeMap.remove(uniqueId);

        return accountMap.remove(uniqueId, account);
    }

    @Override
    public int removeAccounts(@NotNull Predicate<? super Account> filter) {
        MutableIncrement i = MutableIncrement.zero();

        getAccounts().forEach(account -> {
            boolean toRemove = filter.test(account);
            if (!toRemove) return;

            i.increment();

            accountMap.remove(account.getUniqueId());
        });

        return i.get();
    }

    @Override
    public void clearAccounts() {
        accountMap.clear();
    }

    /// Freezing

    private boolean frozen;

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public synchronized void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /// Withdrawable Balance

    @Override
    public double getWithdrawableBalance(@Nullable UUID uniqueId) {
        return getWithdrawableBalance(getAccount(uniqueId));
    }

    @Override
    public double getWithdrawableBalance(@Nullable OfflinePlayer player) {
        return getWithdrawableBalance(getAccount(player));
    }

    @Override
    public double getWithdrawableBalance(@Nullable Account account) {
        if (account == null || frozen || account.isWalletFrozen()) return 0;
        return account.getWallet().getBalance();
    }

    @Override
    public long getWithdrawableCoinBalance(@Nullable UUID uniqueId) {
        return getWithdrawableCoinBalance(getAccount(uniqueId));
    }

    @Override
    public long getWithdrawableCoinBalance(@Nullable OfflinePlayer player) {
        return getWithdrawableCoinBalance(getAccount(player));
    }

    @Override
    public long getWithdrawableCoinBalance(@Nullable Account account) {
        if (account == null || frozen || account.isWalletFrozen()) return 0;
        return account.getWallet().getCoinBalance();
    }

    /// Actions

    @Override
    public @NotNull EconomyResult depositBalance(@NotNull Account account, double amount) {
        return depositBalance(account, amount, EconomyCause.NATIVE_DEPOSIT, null);
    }

    @Override
    public @NotNull EconomyResult depositBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause) {
        return depositBalance(account, amount, cause, null);
    }

    @Override
    public @NotNull EconomyResult depositBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause, @Nullable String message) {
        if (frozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        Wallet wallet = account.getWallet();

        wallet.modifyBalance(amount, cause, message);
        return Objects.equals(cause, EconomyCause.VAULT_DEPOSIT) ? EconomyResult.SUCCESS_VAULT : EconomyResult.SUCCESS_NATIVE;
    }

    @Override
    public @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount) {
        return transferBalance(sender, recipient, amount, null, null);
    }

    @Override
    public @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount, @Nullable EconomyCause cause) {
        return transferBalance(sender, recipient, amount, cause, null);
    }

    @Override
    public @NotNull List<EconomyResult> transferBalance(@NotNull Account sender, @NotNull Account recipient, double amount, @Nullable EconomyCause cause, @Nullable String message) {
        Vector<EconomyResult> results = new Vector<>();

        EconomyResult withdraw = withdrawBalance(sender, amount, cause, message);
        results.add(withdraw);

        if (withdraw.isSuccess()) {
            results.add(depositBalance(recipient, amount, cause, message));
        }

        return List.copyOf(results);
    }

    @Override
    public @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount) {
        return withdrawBalance(account, amount, EconomyCause.NATIVE_WITHDRAWAL, null);
    }

    @Override
    public @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause) {
        return withdrawBalance(account, amount, cause, null);
    }

    @Override
    public @NotNull EconomyResult withdrawBalance(@NotNull Account account, double amount, @Nullable EconomyCause cause, @Nullable String message) {
        if (frozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        Wallet wallet = account.getWallet();

        if (wallet.getBalance() < amount) return EconomyResult.FAILURE_INSUFFICIENT_FUNDS;

        wallet.modifyBalance(-amount, cause, message);
        return Objects.equals(cause, EconomyCause.VAULT_WITHDRAWAL) ? EconomyResult.SUCCESS_VAULT : EconomyResult.SUCCESS_NATIVE;
    }

    @Override
    public @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount) {
        return depositCoinBalance(account, amount, EconomyCause.NATIVE_DEPOSIT, null);
    }

    @Override
    public @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause) {
        return depositCoinBalance(account, amount, cause, null);
    }

    @Override
    public @NotNull EconomyResult depositCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause, @Nullable String message) {
        if (frozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        Wallet wallet = account.getWallet();

        wallet.modifyCoinBalance(amount, cause, message);
        return Objects.equals(cause, EconomyCause.VAULT_DEPOSIT) ? EconomyResult.SUCCESS_VAULT : EconomyResult.SUCCESS_NATIVE;
    }

    @Override
    public @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount) {
        return transferCoinBalance(sender, recipient, amount, null, null);
    }

    @Override
    public @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount, @Nullable EconomyCause cause) {
        return transferCoinBalance(sender, recipient, amount, cause, null);
    }

    @Override
    public @NotNull List<EconomyResult> transferCoinBalance(@NotNull Account sender, @NotNull Account recipient, long amount, @Nullable EconomyCause cause, @Nullable String message) {
        Vector<EconomyResult> results = new Vector<>();

        EconomyResult withdraw = withdrawCoinBalance(sender, amount, cause, message);
        results.add(withdraw);

        if (withdraw.isSuccess()) {
            results.add(depositCoinBalance(recipient, amount, cause, message));
        }

        return List.copyOf(results);
    }

    @Override
    public @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount) {
        return withdrawCoinBalance(account, amount, EconomyCause.NATIVE_WITHDRAWAL, null);
    }

    @Override
    public @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause) {
        return withdrawCoinBalance(account, amount, cause, null);
    }

    @Override
    public @NotNull EconomyResult withdrawCoinBalance(@NotNull Account account, long amount, @Nullable EconomyCause cause, @Nullable String message) {
        if (frozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        Wallet wallet = account.getWallet();

        if (wallet.getCoinBalance() < amount) return EconomyResult.FAILURE_INSUFFICIENT_FUNDS;

        wallet.modifyCoinBalance(-amount, cause, message);
        return Objects.equals(cause, EconomyCause.VAULT_WITHDRAWAL) ? EconomyResult.SUCCESS_VAULT : EconomyResult.SUCCESS_NATIVE;
    }

    /// Transient

    private transient final @NotNull List<DirectMessage> directMessages;
    private transient final @NotNull List<TeleportRequest> teleportRequests;
    private transient final @NotNull Map<UUID, LocalDateTime> lastActivityTimeMap;

    @Override
    public @NotNull List<DirectMessage> getDirectMessages() {
        return List.copyOf(directMessages);
    }

    @Override
    public void addDirectMessage(@NotNull DirectMessage message) {
        directMessages.add(message);
    }

    @Override
    public void clearDirectMessages(@NotNull LocalDateTime cutoff) {
        directMessages.removeIf(dm -> dm.time().isBefore(cutoff));
    }

    @Override
    public void clearDirectMessages() {
        directMessages.clear();
    }

    @Override
    public @NotNull List<TeleportRequest> getTeleportRequests() {
        return List.copyOf(teleportRequests);
    }

    @Override
    public void addTeleportRequest(@NotNull TeleportRequest request) {
        teleportRequests.add(request);
    }

    @Override
    public void removeTeleportRequest(@NotNull TeleportRequest request) {
        teleportRequests.remove(request);
    }

    @Override
    public void clearTeleportRequests(@NotNull LocalDateTime cutoff) {
        teleportRequests.removeIf(tr -> tr.time().isBefore(cutoff));
    }

    @Override
    public void clearTeleportRequests() {
        teleportRequests.clear();
    }

    @Override
    public @NotNull Set<Account> getAfkAccounts() {
        return accountMap.values().stream()
                .filter(this::isAfk)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isAfk(@Nullable UUID uniqueId) {
        LocalDateTime lastActivity = lastActivityTimeMap.getOrDefault(uniqueId, LocalDateTime.now());
        LocalDateTime afkCutoff = LocalDateTime.now().minusMinutes(AFK_MINUTES);

        return lastActivity.isBefore(afkCutoff);
    }

    @Override
    public boolean isAfk(@Nullable OfflinePlayer player) {
        return player != null && isAfk(player.getUniqueId());
    }

    @Override
    public boolean isAfk(@Nullable Account account) {
        return account != null && isAfk(account.getUniqueId());
    }

    @Override
    public void logAccountActivity(@NotNull Account account) {
        lastActivityTimeMap.put(account.getUniqueId(), LocalDateTime.now());
    }

    @Override
    public void clearAfkLogs() {
        lastActivityTimeMap.clear();
    }

    /// Statistics

    @Override
    public double getMoneySupply() {
        return getAccounts().stream()
                .map(Account::getWallet)
                .mapToDouble(Wallet::getBalance)
                .sum();
    }

    @Override
    public long getCoinSupply() {
        return getAccounts().stream()
                .map(Account::getWallet)
                .mapToLong(Wallet::getCoinBalance)
                .sum();
    }

    /// Input/Output

    @Override
    public void clearTransient() {
        clearDirectMessages();
        clearTeleportRequests();
        clearAfkLogs();
    }

    @Override
    public void clearAll() {
        clearTransient();
        clearAccounts();
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void load() throws IOException {
        clearAll();
    }
}
