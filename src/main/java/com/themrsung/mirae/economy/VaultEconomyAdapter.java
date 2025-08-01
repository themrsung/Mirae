package com.themrsung.mirae.economy;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.event.economy.EconomyCause;
import com.themrsung.mirae.state.State;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

/**
 * Vault Economy Adapter.
 */
public final class VaultEconomyAdapter implements Economy {
    /**
     * Creates a new Vault adapter.
     *
     * @param state The state
     * @return The adapter
     */
    public static @NotNull Economy createAdapter(@NotNull State state) {
        return new VaultEconomyAdapter(state);
    }

    /**
     * Creates a new adapter.
     *
     * @param state The state
     */
    private VaultEconomyAdapter(@NotNull State state) {
        this.state = state;
    }

    private final @NotNull State state;

    /**
     * Returns the state instance.
     *
     * @return The state instance
     */
    public @NotNull State getState() {
        return state;
    }

    @Override
    public boolean isEnabled() {
        return Mirae.getInstance().isEnabled();
    }

    @Override
    public String getName() {
        return "Mirae Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return NumberFormat.getNumberInstance().format(v);
    }

    @Override
    public String currencyNamePlural() {
        return "원";
    }

    @Override
    public String currencyNameSingular() {
        return "원";
    }

    @Override
    public boolean hasAccount(String s) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return state.hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return state.hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String s) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return state.getWithdrawableBalance(offlinePlayer);
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return state.getWithdrawableBalance(offlinePlayer);
    }

    @Override
    public boolean has(String s, double v) {
        return has(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return state.getWithdrawableBalance(offlinePlayer) >= v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        Account account = state.getAccount(offlinePlayer);
        if (account == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found.");
        }

        EconomyResult result = state.withdrawBalance(account, v, EconomyCause.VAULT_WITHDRAWAL);

        if (result.isSuccess()) {
            return new EconomyResponse(v, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, result.getMessage());
        } else {
            return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, result.getMessage());
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        Account account = state.getAccount(offlinePlayer);
        if (account == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found.");
        }

        EconomyResult result = state.depositBalance(account, v, EconomyCause.VAULT_DEPOSIT);

        if (result.isSuccess()) {
            return new EconomyResponse(v, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, result.getMessage());
        } else {
            return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.FAILURE, result.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        Account account = Account.createAccount(offlinePlayer.getUniqueId());
        return state.addAccount(account);
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }
}
