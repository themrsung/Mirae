package com.themrsung.mirae.task.player;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTier;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.text.NumberFormat;

/**
 * Scoreboard task.
 */
public class ScoreboardTask implements Runnable {
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

            Component fullName = tier.append(Component.text(account.getTier() != AccountTier.DEFAULT ? " " : ""))
                    .append(displayName);

            Component localChat = account.inLocalChat() ? Component.text("지역채팅").style(MX.STYLE_WARNING) : Component.text("전체채팅").style(MX.STYLE_GOOD);

            Component balance = Component.text(MX.formatBalance(account.getBalance())).style(MX.STYLE_GOOD);
            Component coinBalance = Component.text(MX.formatCoinBalance(account.getCoinBalance())).style(MX.STYLE_GOOD);

            double totalMoneySupply = Mirae.getState().getMoneySupply();
            double ten_thousands = Math.round(totalMoneySupply / 1000000);
            Component moneySupply = Component.text(NumberFormat.getInstance().format(ten_thousands) + "백만원").style(MX.STYLE_SPECIAL);

            Objective obj = board.registerNewObjective("test", Criteria.DUMMY, MiniMessage.miniMessage().deserialize("<gradient:#2e2727:#ff2e01><bold>MIRAE SERVER<reset>"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            int i = 100;

            Score score1 = obj.getScore("Display Name");
            score1.customName(fullName);
            score1.setScore(i--);

            Score score2 = obj.getScore("Local");
            score2.customName(Component.text("채팅: ").style(MX.STYLE_NORMAL).append(localChat));
            score2.setScore(i--);

//            Score score3 = obj.getScore("Empty1");
//            score3.customName(Component.empty());
//            score3.setScore(i--);

            Score score4 = obj.getScore("Balance");
            score4.customName(Component.text("잔액: ").style(MX.STYLE_NORMAL).append(balance));
            score4.setScore(i--);

//            Score score5 = obj.getScore("Coin Balance");
//            score5.customName(Component.text("코인: ").style(MX.STYLE_NORMAL).append(coinBalance));
//            score5.setScore(i--);

            Score score6 = obj.getScore("Empty2");
            score6.customName(Component.empty());
            score6.setScore(i--);

            Score score7 = obj.getScore("Money Supply");
            score7.customName(Component.text("통화량: ").style(MX.STYLE_NORMAL).append(moneySupply));
            score7.setScore(i--);

            Score score8 = obj.getScore("Empty3");
            score8.customName(Component.empty());
            score8.setScore(i--);

            for (SkillType type : SkillType.values()) {
                Score score = obj.getScore("Skill: " + type.toString());
                score.customName(type.getDisplayName()
                        .append(Component.text(": ").style(MX.STYLE_NORMAL))
                        .append(Component.text(account.getSkillLevel(type) + "레벨").style(MX.STYLE_GOOD)));

                score.setScore(i--);
            }

            p.setScoreboard(board);
        });
    }
}
