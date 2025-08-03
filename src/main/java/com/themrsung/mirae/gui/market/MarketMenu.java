package com.themrsung.mirae.gui.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.gui.AbstractGUI;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.market.MarketCategory;
import com.themrsung.mirae.market.PriceQueryResult;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Predicate;

/**
 * Market menu class.
 */
public class MarketMenu extends AbstractGUI {
    /**
     * The GUI size.
     */
    public static final int GUI_SIZE = 54;

    /**
     * The page size.
     */
    public static final int PAGE_SIZE = 45;

    /**
     * Creates a new market menu.
     *
     * @param player The player
     */
    public MarketMenu(@NotNull Player player) {
        super(player, GUI_SIZE, Component.text("전체 상점").style(MX.STYLE_SPECIAL));

        this.markets = Mirae.getState().getMarkets();

        initializeTest();
    }

    /**
     * Creates a new market menu.
     *
     * @param player   The player
     * @param category The category
     */
    public MarketMenu(@NotNull Player player, @NotNull MarketCategory category) {
        this(player, category, null);
    }

    /**
     * Creates a new market menu.
     *
     * @param player   The player
     * @param category The category
     * @param title    The title
     */
    public MarketMenu(@NotNull Player player, @NotNull MarketCategory category, @Nullable Component title) {
        super(player, GUI_SIZE, Objects.requireNonNullElse(title, category.getDisplayName()));

        this.markets = Mirae.getState().getMarkets().stream()
                .filter(m -> m.getCategory() == category)
                .toList();

        initializeTest();
    }

    /**
     * Creates a new market menu.
     *
     * @param player The player
     * @param filter The filter
     * @param title  The title
     */
    public MarketMenu(@NotNull Player player, @NotNull Predicate<? super Market> filter, @NotNull Component title) {
        super(player, GUI_SIZE, title);

        Vector<Market> markets = new Vector<>();

        Mirae.getState().getMarkets().stream()
                .filter(filter)
                .forEach(markets::add);

        this.markets = List.copyOf(markets);

        initializeTest();
    }

    private final List<Market> markets;
    private int updateTask;

    private void initializeTest() {
        updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Mirae.getInstance(), this::updateMarketData, 10, 1);
        updateMarketData();
    }

    private void updateMarketData() {
        for (int i = 0; i < Math.min(markets.size(), PAGE_SIZE); i++) {
            Market market = markets.get(i);
            ItemStack item = market.getItem();
            int stackSize = item.getType().getMaxStackSize();

            PriceQueryResult buyOne = market.getBuyPrice(1);
            PriceQueryResult buyStack = market.getBuyPrice(stackSize);

            PriceQueryResult sellOne = market.getSellPrice(1);
            PriceQueryResult sellStack = market.getSellPrice(stackSize);

            int itemsInInventory = MX.countItems(player.getInventory(), item);
            PriceQueryResult sellAll = market.getSellPrice(itemsInInventory);

            List<String> rawLore = List.of(
                    "1개 구매: " + MX.formatBalance(buyOne.volume()) + " (좌클릭)",
                    buyStack.quantity() + "개 구매: " + MX.formatBalance(buyStack.volume()) + " (Shift + 좌클릭)",
                    "1개 판매: " + MX.formatBalance(sellOne.volume()) + " (우클릭)",
                    sellStack.quantity() + "개 판매: " + MX.formatBalance(sellStack.volume()) + " (Shift + 우클릭)",
                    sellAll.quantity() + "개 판매: " + MX.formatBalance(sellAll.volume()) + " (Q)",
                    "매수잔량: " + market.getTotalBidQuantity() + " / 매도잔량: " + market.getTotalAskQuantity()
            );

            List<Component> lore = rawLore.stream().map(l -> (Component) Component.text(l)).toList();

            ItemMeta meta = item.getItemMeta();

            List<Component> existingLore = meta.lore();
            List<Component> finalLore = new ArrayList<>();

            if (existingLore != null) {
                finalLore.addAll(existingLore);
                if (!existingLore.isEmpty()) finalLore.add(Component.empty());
            }

            finalLore.addAll(lore);

            meta.lore(finalLore);

            item.setItemMeta(meta);

            inventory.setItem(i, item);
        }

        player.updateInventory();
    }

    //
    //
    ///
    // TODO ADD PAGES
    //
    //

    /// //

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        int slot = e.getSlot();
        if (slot >= markets.size()) return;

        Market market = markets.get(slot);
        switch (e.getClick()) {
            case LEFT -> market.buy(account, player.getInventory(), 1);
            case SHIFT_LEFT -> market.buy(account, player.getInventory(), 64); // TODO
            case RIGHT -> market.sell(account, player.getInventory(), 1);
            case SHIFT_RIGHT -> market.sell(account, player.getInventory(), 64); // TODO
        }

        updateMarketData();
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        Bukkit.getScheduler().cancelTask(updateTask);
    }
}
