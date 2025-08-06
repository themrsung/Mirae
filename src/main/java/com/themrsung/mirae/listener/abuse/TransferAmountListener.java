package com.themrsung.mirae.listener.abuse;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.event.economy.AccountBalanceModifiedEvent;
import com.themrsung.mirae.util.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Listens for transfer amounts.
 */
public class TransferAmountListener implements Listener {
    /**
     * Creates a new listener.
     */
    public TransferAmountListener() {
        this.transactionMap = new ConcurrentHashMap<>();
        this.transactionCountMap = new ConcurrentHashMap<>();
        this.events = Collections.synchronizedList(new ArrayList<>());
    }

    public static final double NOTIFY_ADMIN_THRESHOLD = 10;

    private final @NotNull Map<Pair<Account>, Double> transactionMap;
    private final @NotNull Map<Pair<Account>, Long> transactionCountMap;
    private final @NotNull List<AccountBalanceModifiedEvent> events;

    @EventHandler
    public void onTransaction(AccountBalanceModifiedEvent e) {
        if (!Objects.equals(e.getCause(), EconomyCause.NATIVE_TRANSFER)) return;

        events.add(e);
        recordTransactions();
    }

    private void recordTransactions() {
        AccountBalanceModifiedEvent latest = events.getLast();

        for (AccountBalanceModifiedEvent event : List.copyOf(events)) {
            if (event.getBalanceChange() == -latest.getBalanceChange()) {
                events.remove(latest);
                events.remove(event);

                List<Account> ordered = Stream.of(latest.getAccount(), event.getAccount())
                        .sorted(Comparator.comparing(Account::getUniqueId))
                        .toList();

                Pair<Account> pair = new Pair<>(ordered.getFirst(), ordered.getLast());

                double existing = transactionMap.getOrDefault(pair, 0d);
                transactionMap.put(pair, existing += event.getAbsoluteChange());

                long existingCount = transactionCountMap.getOrDefault(pair, 0L);
                transactionCountMap.put(pair, existingCount += 1);

                return;
            }
        }
    }

    public void processTransactions() {
        transactionCountMap.forEach((pair, count) -> {
            if (count < NOTIFY_ADMIN_THRESHOLD) return;

            double amount = transactionMap.getOrDefault(pair, 0d);

            Component message = Component.text("[").style(MX.STYLE_NORMAL)
                    .append(Component.text("!").style(MX.STYLE_ERROR))
                    .append(Component.text("]").style(MX.STYLE_NORMAL))
                    .appendSpace()
                    .append(Component.text("이상 거래 의심: ").style(MX.STYLE_NORMAL))
                    .append(Component.text(pair.first().getName()).style(MX.STYLE_SPECIAL))
                    .append(Component.text(" <-> ").style(MX.STYLE_NORMAL))
                    .append(Component.text(pair.second().getName()).style(MX.STYLE_SPECIAL))
                    .appendSpace()
                    .append(Component.text("(거래금액: ").style(MX.STYLE_NORMAL))
                    .append(Component.text(MX.formatBalance(amount)).style(MX.STYLE_SPECIAL))
                    .append(Component.text(" / 거래횟수: ").style(MX.STYLE_NORMAL))
                    .append(Component.text(count + "회").style(MX.STYLE_SPECIAL))
                    .append(Component.text(")").style(MX.STYLE_NORMAL));

            Bukkit.getOperators().forEach(op -> {
                Account account = Mirae.getState().getAccount(op);
                if (account == null) return;

                account.sendMessage(message);
            });

            Bukkit.getConsoleSender().sendMessage(message);

        });

        transactionMap.clear();
        transactionCountMap.clear();
        events.clear();
    }
}
