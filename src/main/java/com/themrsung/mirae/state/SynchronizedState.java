package com.themrsung.mirae.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.gson.*;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.market.active.Fulfillment;
import com.themrsung.mirae.market.active.OrderChain;
import com.themrsung.mirae.social.DirectMessage;
import com.themrsung.mirae.social.TeleportRequest;
import com.themrsung.mirae.util.Coordinate;
import com.themrsung.mirae.util.MutableIncrement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Default synchronized implementation of {@link State}.
 */
public class SynchronizedState implements State {
    /**
     * Creates an empty state.
     */
    SynchronizedState() {
        // Non-transient
        this.spawnPoint = null;
        this.accountMap = new ConcurrentHashMap<>();
        this.marketMap = new ConcurrentHashMap<>();

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

    /// Markets

    private final @NotNull Map<UUID, Market> marketMap;

    @Override
    public @NotNull Map<UUID, Market> getMarketMap() {
        return Map.copyOf(marketMap);
    }

    @Override
    public @NotNull List<Market> getMarkets() {
        return List.of();
    }

    @Override
    public @Nullable Market getMarket(@Nullable UUID uniqueId) {
        return marketMap.get(uniqueId);
    }

    @Override
    public boolean hasMarket(@Nullable Market market) {
        return false;
    }

    @Override
    public boolean addMarket(@NotNull Market market) {
        UUID uniqueId = market.getUniqueId();
        if (marketMap.containsKey(uniqueId)) {
            return false;
        }

        marketMap.put(market.getUniqueId(), market);
        return true;
    }

    @Override
    public boolean removeMarket(@NotNull Market market) {
        return marketMap.remove(market.getUniqueId(), market);
    }

    @Override
    public void clearMarkets() {
        marketMap.clear();
    }

    /// Freezing

    private boolean economyFrozen;

    @Override
    public boolean isEconomyFrozen() {
        return economyFrozen;
    }

    @Override
    public synchronized void setEconomyFrozen(boolean frozen) {
        this.economyFrozen = frozen;
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
        if (account == null || economyFrozen || account.isWalletFrozen()) return 0;
        return account.getBalance();
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
        if (account == null || economyFrozen || account.isWalletFrozen()) return 0;
        return account.getCoinBalance();
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
        if (economyFrozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;

        account.modifyBalance(amount, cause, message);
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
        if (economyFrozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        if (account.getBalance() < amount) return EconomyResult.FAILURE_INSUFFICIENT_FUNDS;

        account.modifyBalance(-amount, cause, message);
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
        if (economyFrozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;

        account.modifyCoinBalance(amount, cause, message);
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
        if (economyFrozen) return EconomyResult.FAILURE_ECONOMY_FROZEN;
        if (account.isWalletFrozen()) return EconomyResult.FAILURE_ACCOUNT_FROZEN;

        if (account.getCoinBalance() < amount) return EconomyResult.FAILURE_INSUFFICIENT_FUNDS;

        account.modifyCoinBalance(-amount, cause, message);
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
        Set<UUID> operatorIds = Bukkit.getOperators().stream()
                .map(OfflinePlayer::getUniqueId)
                .collect(Collectors.toUnmodifiableSet());

        return getAccounts().stream()
                .filter(a -> !operatorIds.contains(a.getUniqueId()))
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public long getCoinSupply() {
        Set<UUID> operatorIds = Bukkit.getOperators().stream()
                .map(OfflinePlayer::getUniqueId)
                .collect(Collectors.toUnmodifiableSet());

        return getAccounts().stream()
                .filter(a -> !operatorIds.contains(a.getUniqueId()))
                .mapToLong(Account::getCoinBalance)
                .sum();
    }

    /// Input/Output

    private static final @NotNull Gson SERIALIZER = new GsonBuilder()
            .registerTypeHierarchyAdapter(Account.class, Account.serializer())
            .registerTypeHierarchyAdapter(Market.class, Market.serializer())
            .registerTypeAdapter(Fulfillment.class, Fulfillment.serializer())
            .registerTypeAdapter(OrderChain.class, OrderChain.serializer())
            .registerTypeAdapter(Coordinate.class, Coordinate.serializer())
            .registerTypeAdapter(ItemStack.class, ItemStackGson.serializer())
            .registerTypeAdapter(SkillTypeLongPair.class, SkillTypeLongPair.serializer())
            .registerTypeAdapter(StringCoordinatePair.class, StringCoordinatePair.serializer())
            .registerTypeAdapter(StateData.class, StateData.serializer())
            .registerTypeAdapter(LocalDateTime.class, LocalDateTimeGson.serializer())
            .setPrettyPrinting()
            .create();

    private static final @NotNull Gson DESERIALIZER = new GsonBuilder()
            .registerTypeHierarchyAdapter(Account.class, Account.deserializer())
            .registerTypeHierarchyAdapter(Market.class, Market.deserializer())
            .registerTypeAdapter(Fulfillment.class, Fulfillment.deserializer())
            .registerTypeAdapter(OrderChain.class, OrderChain.deserializer())
            .registerTypeAdapter(Coordinate.class, Coordinate.deserializer())
            .registerTypeAdapter(ItemStack.class, ItemStackGson.deserializer())
            .registerTypeAdapter(SkillTypeLongPair.class, SkillTypeLongPair.deserializer())
            .registerTypeAdapter(StringCoordinatePair.class, StringCoordinatePair.deserializer())
            .registerTypeAdapter(StateData.class, StateData.deserializer())
            .registerTypeAdapter(LocalDateTime.class, LocalDateTimeGson.deserializer())
            .create();

    private static final @NotNull String SAVE_PATH = "plugins/Mirae";

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
        clearMarkets();
    }

    @Override
    public void save() throws IOException {
        File pluginDir = new File(SAVE_PATH);
        if (!pluginDir.exists() && !pluginDir.mkdirs()) {
            throw new IOException("Failed to create plugin directory.");
        }

        File backupsDir = new File(SAVE_PATH + "/backups");
        if (!backupsDir.exists() && !backupsDir.mkdirs()) {
            throw new IOException("Unable to create backups folder.");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File backupFile = new File(backupsDir, timestamp + ".zip");

        try (FileOutputStream fos = new FileOutputStream(backupFile)) {
            ZipOutputStream zos = new ZipOutputStream(fos);

            Files.walk(pluginDir.toPath())
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> !path.startsWith(backupsDir.toPath()))
                    .forEach(path -> {
                        try {
                            ZipEntry entry = new ZipEntry(pluginDir.toPath().relativize(path).toString());
                            zos.putNextEntry(entry);

                            byte[] bytes = Files.readAllBytes(path);

                            zos.write(bytes);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException("Error creating backup file.", e);
                        }
                    });

            zos.close();
        } catch (UncheckedIOException e) {
            throw new IOException(e);
        }

        File dataFile = new File(SAVE_PATH + "/data.json");
        try (FileWriter writer = new FileWriter(dataFile)) {
            StateData data = new StateData(spawnPoint != null ? new Coordinate(spawnPoint) : null);
            writer.write(SERIALIZER.toJson(data));
        } catch (IOException e) {
            throw new IOException("Error saving state data.", e);
        }

        File accountsDir = new File(SAVE_PATH + "/accounts");
        if (!accountsDir.exists() && !accountsDir.mkdirs())
            throw new IOException("Unable to create accounts folder.");

        FileUtils.cleanDirectory(accountsDir);

        accountMap.forEach((uuid, account) -> {
            File file = new File(SAVE_PATH + "/accounts/" + uuid + ".json");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(SERIALIZER.toJson(account));
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException("Error saving account " + uuid, e);
            }
        });

        File marketsDir = new File(SAVE_PATH + "/markets");
        if (!marketsDir.exists() && !marketsDir.mkdirs()) {
            throw new IOException("Unable to create markets folder.");
        }

        FileUtils.cleanDirectory(marketsDir);

        marketMap.forEach((uuid, market) -> {
            File file = new File(SAVE_PATH + "/markets/" + uuid + ".json");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(SERIALIZER.toJson(market));
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException("Error saving market " + uuid, e);
            }
        });
    }

    @Override
    public void load() throws IOException {
        clearAll();

        File accountsDir = new File(SAVE_PATH + "/accounts");
        if (accountsDir.exists()) {
            File[] accountFiles = accountsDir.listFiles();
            if (accountFiles != null) {
                for (File file : accountFiles) {
                    if (!file.getName().endsWith(".json")) continue;

                    try (FileReader reader = new FileReader(file)) {
                        Account account = DESERIALIZER.fromJson(reader, Account.class);
                        UUID accountId = account.getUniqueId();

                        accountMap.put(accountId, account);
                    } catch (IOException e) {
                        throw new IOException("Error loading account file: " + file.getName(), e);
                    }
                }
            }
        }

        File marketsDir = new File(SAVE_PATH + "/markets");
        if (marketsDir.exists()) {
            File[] marketFiles = marketsDir.listFiles();
            if (marketFiles != null) {
                for (File file : marketFiles) {
                    if (!file.getName().endsWith(".json")) continue;

                    try (FileReader reader = new FileReader(file)) {
                        Market market = DESERIALIZER.fromJson(reader, Market.class);
                        UUID marketId = market.getUniqueId();

                        marketMap.put(marketId, market);
                    } catch (IOException e) {
                        throw new IOException("Error loading market file: " + file.getName(), e);
                    }
                }
            }
        }

        File dataFile = new File(SAVE_PATH + "/data.json");
        if (dataFile.exists()) {
            try (FileReader reader = new FileReader(dataFile)) {
                StateData data = DESERIALIZER.fromJson(reader, StateData.class);

                Coordinate s = data.getSpawnPoint();
                if (s != null) try {
                    spawnPoint = s.asLocation();
                } catch (IllegalArgumentException ignored) {
                }

            } catch (IOException e) {
                throw new IOException("Error loading data.", e);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid spawn coordinate.", e);
            }
        }
    }
}
