package com.themrsung.mirae.task.economy;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Interest payout task.
 */
public class InterestPayoutTask implements Runnable {
    /**
     * The base annual interest rate.
     */
    public static final double BASE_RATE = 10;

    /**
     * The maximum allowed daily interest.
     */
    public static final double MAX_DAILY_INTEREST = Double.MAX_VALUE;

    /**
     * Calculates daily interest.
     *
     * @param principal The principal amount
     * @return The interest
     */
    public static double getDailyInterest(double principal) {
        int daysInYear = 365;
        double dailyRate = Math.pow(1 + BASE_RATE, 1d / daysInYear) - 1;

        return Math.min(principal * dailyRate, MAX_DAILY_INTEREST);
    }

    @Override
    public void run() {
        Mirae.getState().getAccounts().forEach(a -> {
            double balance = a.getBalance();
            if (balance <= 0) return;

            Player player = a.getPlayer();
            if (player == null || !player.isOnline()) return;

            // Interest per 10 minutes
            double interest = Math.floor(getDailyInterest(balance) * (1d / 24 / 6));

            if (interest <= 0) return;

            EconomyResult result = Mirae.getState().depositBalance(a, interest, EconomyCause.INTEREST_RECEIVED, "Regular interest payment.");
            if (!result.isSuccess()) {
                Mirae.getInstance().getLogger().warning("Error paying interest to user \"" + a.getUniqueId() + "\".");
                return;
            }

            player.sendMessage(Component.text("[입금] ").style(MX.STYLE_GOOD)
                    .append(Component.text("미래 서버").style(MX.STYLE_SPECIAL))
                    .append(Component.text("에서 이자 ").style(MX.STYLE_NORMAL))
                    .append(Component.text(MX.formatBalance(interest)).style(MX.STYLE_SPECIAL))
                    .append(Component.text("을 보냈습니다.").style(MX.STYLE_NORMAL)));
        });
    }
}
