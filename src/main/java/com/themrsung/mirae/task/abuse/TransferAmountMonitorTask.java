package com.themrsung.mirae.task.abuse;

import com.themrsung.mirae.listener.Listeners;

/**
 * Transfer amount monitor task.
 */
public class TransferAmountMonitorTask implements Runnable {
    @Override
    public void run() {
        Listeners.TRANSFER_AMOUNT_LISTENER.processTransactions();
    }
}
