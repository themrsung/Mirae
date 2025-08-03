package com.themrsung.mirae.task.economy;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.market.active.ActivePriceMarket;

/**
 * Updates markets.
 */
public class MarketUpdateTask implements Runnable {
    @Override
    public void run() {
        Mirae.getState().getMarkets().stream()
                .filter(m -> m instanceof ActivePriceMarket)
                .map(m -> (ActivePriceMarket) m)
                .forEach(ActivePriceMarket::updateServerOrders);
    }
}
