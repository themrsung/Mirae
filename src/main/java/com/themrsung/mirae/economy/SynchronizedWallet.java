package com.themrsung.mirae.economy;

import com.themrsung.mirae.event.economy.EconomyCause;
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
        balance += change;
        return balance;
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
        coinBalance += change;
        return coinBalance;
    }
}
