package com.themrsung.mirae.gui.market;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.gui.AbstractGUI;
import com.themrsung.mirae.market.Market;
import com.themrsung.mirae.market.MarketCategory;
import com.themrsung.mirae.market.OrderResult;
import com.themrsung.mirae.market.PriceQueryResult;
import com.themrsung.mirae.market.active.ActivePriceMarket;
import com.themrsung.mirae.market.fixed.FixedPriceMarket;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
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
     * Safety margin for balance checks.
     */
    public static final double BUY_SAFETY_MARGIN = 0.005;

    /**
     * Creates a new market menu.
     *
     * @param player The player
     */
    public MarketMenu(@NotNull Player player) {
        super(player, GUI_SIZE, Component.text("전체 상점").style(MX.STYLE_SPECIAL));

        this.markets = new ArrayList<>(Mirae.getState().getMarkets());
        this.markets.sort(Comparator.comparing(Market::getName));

        this.currentPage = 0;
        this.numPages = Math.ceilDiv(markets.size(), PAGE_SIZE);

        this.callbacks = new HashMap<>();

        initialize();
    }

    /**
     * Creates a new market menu.
     *
     * @param player   The player
     * @param category The category
     */
    public MarketMenu(@NotNull Player player, @NotNull MarketCategory category) {
        this(player, category, category.getDisplayName());
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

        this.markets = new ArrayList<>(Mirae.getState().getMarkets().stream()
                .filter(m -> m.getCategory() == category)
                .toList());
        this.markets.sort(Comparator.comparing(Market::getName));

        this.currentPage = 0;
        this.numPages = Math.ceilDiv(markets.size(), PAGE_SIZE);

        this.callbacks = new HashMap<>();

        initialize();
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

        this.markets = new ArrayList<>(markets);
        this.markets.sort(Comparator.comparing(Market::getName));

        this.currentPage = 0;
        this.numPages = Math.ceilDiv(markets.size(), PAGE_SIZE);

        this.callbacks = new HashMap<>();

        initialize();
    }

    private final List<Market> markets;
    private int updateTask;

    private int currentPage;
    private final int numPages;

    private final Map<Integer, Consumer<? super InventoryClickEvent>> callbacks;

    private void initialize() {
        updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Mirae.getInstance(), this::updateMarketData, 10, 1);

        renderNavigation();
        updateMarketData();
    }

    private void renderNavigation() {
        /// 52: PREVIOUS
        ItemStack previous = getPreviousButton();
        inventory.setItem(52, previous);
        callbacks.put(52, e -> {
            if (currentPage == 0) {
                player.sendMessage(Component.text("첫번째 페이지입니다.").style(MX.STYLE_WARNING));
            } else {
                currentPage--;
                player.sendMessage(Component.text("이전 페이지로 이동합니다.").style(MX.STYLE_GOOD));
            }

            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            updateMarketData();
        });

        /// 53: NEXT
        ItemStack next = getNextButton();
        inventory.setItem(53, next);
        callbacks.put(53, e -> {
            if (currentPage >= numPages - 1) {
                player.sendMessage(Component.text("마지막 페이지입니다.").style(MX.STYLE_WARNING));
            } else {
                currentPage++;
                player.sendMessage(Component.text("다음 페이지로 이동합니다.").style(MX.STYLE_GOOD));
            }

            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            updateMarketData();
        });
    }

    private void renderBlank(int slot) {
        inventory.setItem(slot, null);
        callbacks.put(slot, null);
    }

    private void renderItem(int slot, Market market) {
        ItemStack item = market.getItem();
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>(Objects.requireNonNullElse(meta.lore(), List.of()));

        int stackSize = item.getType().getMaxStackSize();

        int itemCount = MX.countItems(player.getInventory(), item);

        switch (market) {
            case ActivePriceMarket apm -> {
                PriceQueryResult buyOne = market.getBuyPrice(1);
                if (buyOne.quantity() < 1) {
                    lore.add(Component.text("판매가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text("(재고 없음)").style(MX.STYLE_BUY)));
                } else {
                    lore.add(Component.text("판매가").style(MX.STYLE_NORMAL));
                    lore.add(Component.text("  - 1개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(buyOne.volume())).style(MX.STYLE_BUY))
                            .append(Component.text(" [좌클릭]").style(MX.STYLE_NORMAL)));

                    if (stackSize > 1) {
                        PriceQueryResult buyStack = market.getBuyPrice(stackSize);
                        if (buyStack.quantity() > 1) {
                            lore.add(Component.text("  - " + buyStack.quantity() + "개: ").style(MX.STYLE_NORMAL)
                                    .append(Component.text(MX.formatBalance(buyStack.volume())).style(MX.STYLE_BUY))
                                    .append(Component.text(" [Shift + 좌클릭]").style(MX.STYLE_NORMAL)));
                        }
                    }
                }

                PriceQueryResult sellOne = market.getSellPrice(1);
                if (sellOne.quantity() < 1) {
                    lore.add(Component.text("매입가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text("(매입 중단)").style(MX.STYLE_SELL)));
                } else {
                    lore.add(Component.text("매입가").style(MX.STYLE_NORMAL));
                    lore.add(Component.text("  - 1개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(sellOne.volume())).style(MX.STYLE_SELL))
                            .append(Component.text(" [우클릭]").style(MX.STYLE_NORMAL)));

                    if (stackSize > 1) {
                        PriceQueryResult sellStack = market.getSellPrice(stackSize);
                        if (sellStack.quantity() > 1) {
                            lore.add(Component.text("  - " + sellStack.quantity() + "개: ").style(MX.STYLE_NORMAL)
                                    .append(Component.text(MX.formatBalance(sellStack.volume())).style(MX.STYLE_SELL))
                                    .append(Component.text(" [Shift + 우클릭]").style(MX.STYLE_NORMAL)));

                            PriceQueryResult sellAll = market.getSellPrice(itemCount);

                            lore.add(Component.text("  - " + sellAll.quantity() + "개: ").style(MX.STYLE_NORMAL)
                                    .append(Component.text(MX.formatBalance(sellAll.volume())).style(MX.STYLE_SELL))
                                    .append(Component.text(" [Q]").style(MX.STYLE_NORMAL)));
                        }
                    }
                }
            }

            case FixedPriceMarket fpm -> {
                PriceQueryResult buy = fpm.getBuyPrice(1);
                PriceQueryResult sell = fpm.getSellPrice(1);

                double buyPrice = buy.price();
                double sellPrice = sell.price();

                boolean canBuy = buyPrice >= 0;
                boolean canSell = sellPrice >= 0;

                if (canBuy) {
                    lore.add(Component.text("판매가").style(MX.STYLE_NORMAL));
                    lore.add(Component.text("  - 1개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(buyPrice)).style(MX.STYLE_BUY))
                            .append(Component.text(" [좌클릭]").style(MX.STYLE_NORMAL)));

                    lore.add(Component.text("  - " + stackSize + "개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(buyPrice * stackSize)).style(MX.STYLE_BUY))
                            .append(Component.text(" [Shift + 좌클릭]").style(MX.STYLE_NORMAL)));
                } else {
                    lore.add(Component.text("판매가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text("(재고 없음)").style(MX.STYLE_BUY)));
                }

                if (canSell) {
                    lore.add(Component.text("매입가").style(MX.STYLE_NORMAL));
                    lore.add(Component.text("  - 1개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(sellPrice)).style(MX.STYLE_SELL))
                            .append(Component.text(" [우클릭]").style(MX.STYLE_NORMAL)));

                    lore.add(Component.text("  - " + stackSize + "개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(sellPrice * stackSize)).style(MX.STYLE_SELL))
                            .append(Component.text(" [Shift + 우클릭]").style(MX.STYLE_NORMAL)));

                    lore.add(Component.text("  - " + itemCount + "개: ").style(MX.STYLE_NORMAL)
                            .append(Component.text(MX.formatBalance(sellPrice * itemCount)).style(MX.STYLE_SELL))
                            .append(Component.text(" [Q]").style(MX.STYLE_NORMAL)));
                } else {
                    lore.add(Component.text("매입가: ").style(MX.STYLE_NORMAL)
                            .append(Component.text("(매입 중단)").style(MX.STYLE_SELL)));
                }
            }

            default -> renderBlank(slot);
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);

        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        callbacks.put(slot, e -> {
            switch (e.getClick()) {
                case LEFT -> onBuyClick(account, market, 1);
                case SHIFT_LEFT -> onBuyClick(account, market, stackSize);
                case RIGHT -> onSellClick(account, market, 1);
                case SHIFT_RIGHT -> onSellClick(account, market, stackSize);
                case DROP, CONTROL_DROP -> onSellClick(account, market, itemCount);
            }
        });
    }

    private void onBuyClick(@NotNull Account account, @NotNull Market market, int quantity) {
        PriceQueryResult pqr = market.getBuyPrice(quantity);

        if (pqr.quantity() <= 0) {
            player.sendMessage(Component.text("매수 가능한 물량이 없습니다!").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (Mirae.getState().isEconomyFrozen()) {
            player.sendMessage(Component.text("경제가 동결되었습니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (account.isWalletFrozen()) {
            player.sendMessage(Component.text("계좌가 동결되었습니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        // Calculations are complex to allow negative price trades

        double absoluteVolume = Math.abs(pqr.volume());
        double safetyMargin = absoluteVolume * BUY_SAFETY_MARGIN;
        double minimumBalance = pqr.volume() + safetyMargin;

        if (minimumBalance > 0 && account.getBalance() < minimumBalance) {
            player.sendMessage(Component.text("잔액이 부족합니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        ItemStack items = market.getItem();
        items.setAmount(quantity);

        int inventorySpace = MX.getRemainingSpaceFor(player.getInventory(), items);
        if (inventorySpace < quantity) {
            player.sendMessage(Component.text("인벤토리에 공간이 부족합니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        OrderResult result = market.buy(account, player.getInventory(), quantity);
        notifyPlayer(items, (int) result.quantityFulfilled(), true);
    }

    private void onSellClick(@NotNull Account account, @NotNull Market market, int quantity) {
        if (quantity <= 0) {
            player.sendMessage(Component.text("아이템이 없습니다!").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (Mirae.getState().isEconomyFrozen()) {
            player.sendMessage(Component.text("경제가 동결되었습니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        if (account.isWalletFrozen()) {
            player.sendMessage(Component.text("계좌가 동결되었습니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        ItemStack items = market.getItem();
        items.setAmount(quantity);

        int itemsInInventory = MX.countItems(player.getInventory(), items);
        if (itemsInInventory < quantity) {
            player.sendMessage(Component.text("아이템이 부족합니다.").style(MX.STYLE_ERROR));
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        OrderResult result = market.sell(account, player.getInventory(), quantity);
        notifyPlayer(items, (int) result.quantityFulfilled(), false);
    }

    private void notifyPlayer(@NotNull ItemStack item, int quantity, boolean buy) {
        Component name = Component.text(item.getType().toString()).style(MX.STYLE_SPECIAL);

        player.sendMessage(name
                .appendSpace()
                .append(Component.text(quantity + "개").style(MX.STYLE_SPECIAL))
                .append(Component.text("를 ").style(MX.STYLE_NORMAL))
                .append(buy ? Component.text("구매").style(MX.STYLE_BUY) : Component.text("판매").style(MX.STYLE_SELL))
                .append(Component.text("했습니다.").style(MX.STYLE_NORMAL)));

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    private void updateMarketData() {
        int j = 0;
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, markets.size());

        for (int i = start; i < end; i++) {
            renderItem(j++, markets.get(i));
        }

        // 나머지 칸 빈칸 처리
        while (j < PAGE_SIZE) {
            renderBlank(j++);
        }

        player.updateInventory();
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        if (!Objects.equals(inventory, e.getClickedInventory())) return;
        e.setCancelled(true);

        var callback = callbacks.get(e.getSlot());
        if (callback != null) {
            callback.accept(e);
        }

        updateMarketData();
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        Bukkit.getScheduler().cancelTask(updateTask);
    }
}
