package com.themrsung.mirae.economy;

import com.themrsung.mirae.event.economy.EconomyCause;
import com.themrsung.mirae.event.economy.WalletBalanceModifiedEvent;
import com.themrsung.mirae.event.economy.WalletCoinBalanceModifiedEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

/**
 * Default synchronized implementation of {@link Wallet}.
 */
public class SynchronizedWallet implements Wallet {
    /**
     * Creates a new wallet.
     */
    SynchronizedWallet() {
        this(0, 0);
    }

    /**
     * Creates a new wallet.
     *
     * @param balance     The balance
     * @param coinBalance The coin balance
     */
    SynchronizedWallet(double balance, long coinBalance) {
        this.balance = balance;
        this.coinBalance = coinBalance;
    }

    private double balance;
    private long coinBalance;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public synchronized double modifyBalance(double change) {
        return modifyBalance(change, null, null);
    }

    @Override
    public synchronized double modifyBalance(double change, @Nullable EconomyCause cause) {
        return modifyBalance(change, cause, null);
    }

    @Override
    public synchronized double modifyBalance(double change, @Nullable EconomyCause cause, @Nullable String message) {
        double balanceBefore = balance;
        balance += change;
        double balanceAfter = balance;

        var event = WalletBalanceModifiedEvent.builder()
                .wallet(this)
                .cause(cause)
                .message(message)
                .change(change)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        Bukkit.getPluginManager().callEvent(event);

        return balanceAfter;
    }

    @Override
    public long getCoinBalance() {
        return coinBalance;
    }

    @Override
    public synchronized long modifyCoinBalance(long change) {
        return modifyCoinBalance(change, null, null);
    }

    @Override
    public synchronized long modifyCoinBalance(long change, @Nullable EconomyCause cause) {
        return modifyCoinBalance(change, cause, null);
    }

    @Override
    public synchronized long modifyCoinBalance(long change, @Nullable EconomyCause cause, @Nullable String message) {
        long balanceBefore = coinBalance;
        coinBalance += change;
        long balanceAfter = coinBalance;

        var event = WalletCoinBalanceModifiedEvent.builder()
                .wallet(this)
                .cause(cause)
                .message(message)
                .change(change)
                .coinBalanceBefore(balanceBefore)
                .coinBalanceAfter(balanceAfter)
                .build();

        Bukkit.getPluginManager().callEvent(event);

        return balanceAfter;
    }
}
