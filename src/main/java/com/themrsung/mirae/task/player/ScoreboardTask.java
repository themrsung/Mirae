package com.themrsung.mirae.task.player;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public final class ScoreboardTask implements Runnable {
    @Override
    public void run() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Bukkit.getOnlinePlayers().forEach(p -> {
            Account account = Mirae.getState().getAccount(p);
            Scoreboard board = manager.getNewScoreboard();

            if (account == null || account.hideScoreboard()) {
                p.setScoreboard(board);
                return;
            }

            Component displayName = account.getDisplayName(MX.STYLE_SPECIAL);
            Component tier = account.getTier().getDisplayName();

            Component fullName = tier.append(Component.text(" "))
                    .append(displayName);

            Component balance = Component.text(MX.formatBalance(account.getBalance())).style(MX.STYLE_GOOD);
            Component coinBalance = Component.text(MX.formatCoinBalance(account.getCoinBalance())).style(MX.STYLE_GOOD);
            Component moneySupply = Component.text(MX.formatBalance(Mirae.getState().getMoneySupply())).style(MX.STYLE_SPECIAL);

            Objective obj = board.registerNewObjective("test", Criteria.DUMMY, MiniMessage.miniMessage().deserialize("<gradient:#2e2727:#ff2e01><bold>MIRAE SERVER<reset>"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score score1 = obj.getScore("Display Name");
            score1.customName(fullName);
            score1.setScore(100);

            Score score2 = obj.getScore("Empty1");
            score2.customName(Component.empty());
            score2.setScore(99);

            Score score3 = obj.getScore("Balance");
            score3.customName(Component.text("잔액: ").style(MX.STYLE_NORMAL).append(balance));
            score3.setScore(98);

            Score score4 = obj.getScore("Coin Balance");
            score4.customName(Component.text("코인: ").style(MX.STYLE_NORMAL).append(coinBalance));
            score4.setScore(97);

            Score score5 = obj.getScore("Empty2");
            score5.customName(Component.empty());
            score5.setScore(96);

            Score score6 = obj.getScore("Money Supply");
            score6.customName(Component.text("통화량: ").style(MX.STYLE_NORMAL).append(moneySupply));
            score6.setScore(95);

            p.setScoreboard(board);
        });
    }
}
