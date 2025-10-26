package com.themrsung.mirae.gui.storage;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.gui.AbstractGUI;
import com.themrsung.mirae.item.storage.ItemStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * GUI for the personal item storage.
 */
public final class ItemStorageMenu extends AbstractGUI {
    private static final int INVENTORY_SIZE = 54;
    private static final int ITEMS_PER_PAGE = 36;
    private static final @NotNull List<Integer> ITEM_SLOTS = IntStream.range(0, ITEMS_PER_PAGE).boxed().toList();
    private static final @NotNull Set<Integer> DEPOSIT_SLOTS = IntStream.range(45, 54).boxed().collect(Collectors.toSet());

    private final @NotNull ItemStorage storage;
    private final @NotNull Map<Integer, ItemStack> slotKeyMap;
    private int page;
    private boolean hasNextPage;

    public ItemStorageMenu(@NotNull Player player, @NotNull Account account) {
        super(player, INVENTORY_SIZE, Component.text("개인 창고").style(MX.STYLE_SPECIAL));
        this.storage = account.getItemStorage();
        this.slotKeyMap = new HashMap<>();
        this.page = 0;
        this.hasNextPage = false;

        initializeStaticComponents();
        renderPage();
    }

    private void initializeStaticComponents() {
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(Component.text("사용 방법").style(MX.STYLE_SPECIAL));
        infoMeta.lore(List.of(
                Component.text("좌클릭: 최대 64개 인출").style(MX.STYLE_NORMAL),
                Component.text("우클릭: 1개 인출").style(MX.STYLE_NORMAL),
                Component.text("Shift + 좌클릭: 전부 인출").style(MX.STYLE_NORMAL),
                Component.text("하단 9칸에 넣으면 자동 보관").style(MX.STYLE_NORMAL),
                Component.text("Shift + 클릭으로 바로 보관").style(MX.STYLE_NORMAL)
        ));
        info.setItemMeta(infoMeta);
        inventory.setItem(40, info);

        ItemStack closeButton = getCancelButton();
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.displayName(Component.text("닫기").style(MX.STYLE_NORMAL));
        closeButton.setItemMeta(closeMeta);
        inventory.setItem(43, closeButton);
    }

    private void renderPage() {
        slotKeyMap.clear();
        ITEM_SLOTS.forEach(slot -> inventory.setItem(slot, null));

        List<Map.Entry<ItemStack, Long>> entries = storage.asEntryList();
        int maxPage = entries.isEmpty() ? 0 : (entries.size() - 1) / ITEMS_PER_PAGE;
        if (page > maxPage) {
            page = maxPage;
        }

        boolean hasPrevious = page > 0;
        hasNextPage = page < maxPage;

        inventory.setItem(36, hasPrevious ? getPreviousButton() : null);
        inventory.setItem(44, hasNextPage ? getNextButton() : null);

        int startIndex = page * ITEMS_PER_PAGE;
        for (int index = 0; index < ITEMS_PER_PAGE; index++) {
            int entryIndex = startIndex + index;
            if (entryIndex >= entries.size()) break;

            Map.Entry<ItemStack, Long> entry = entries.get(entryIndex);
            ItemStack key = entry.getKey();
            long value = entry.getValue();

            ItemStack display = createDisplayItem(key, value);
            int slot = ITEM_SLOTS.get(index);
            inventory.setItem(slot, display);
            slotKeyMap.put(slot, ItemStorage.standardize(key));
        }
    }

    private @NotNull ItemStack createDisplayItem(@NotNull ItemStack base, long amount) {
        ItemStack display = base.clone();
        display.setAmount((int) Math.min(amount, base.getMaxStackSize()));

        ItemMeta meta = display.getItemMeta();
        List<Component> lore = new ArrayList<>();
        List<Component> originalLore = meta.lore();
        if (originalLore != null) {
            lore.addAll(originalLore);
        }
        if (!lore.isEmpty()) {
            lore.add(Component.empty());
        }
        lore.add(Component.text("보관 수량: " + amount).style(MX.STYLE_NORMAL));
        lore.add(Component.text("좌클릭: 최대 64개 인출").style(MX.STYLE_NORMAL));
        lore.add(Component.text("우클릭: 1개 인출").style(MX.STYLE_NORMAL));
        lore.add(Component.text("Shift + 좌클릭: 전부 인출").style(MX.STYLE_NORMAL));
        meta.lore(lore);
        display.setItemMeta(meta);
        return display;
    }

    private void withdrawFromStorage(@NotNull ItemStack key, long amount) {
        long available = storage.getAmount(key);
        if (available <= 0) {
            return;
        }

        long removed = storage.removeItem(key, amount);
        if (removed <= 0) {
            return;
        }

        giveItemsToPlayer(key, removed);
        renderPage();
    }

    private void giveItemsToPlayer(@NotNull ItemStack key, long quantity) {
        Player player = getPlayer();
        long remaining = quantity;
        while (remaining > 0) {
            int stackSize = (int) Math.min(remaining, key.getMaxStackSize());
            ItemStack toGive = key.clone();
            toGive.setAmount(stackSize);
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(toGive);
            if (!leftover.isEmpty()) {
                leftover.values().forEach(item -> player.getWorld().dropItem(player.getLocation(), item));
            }
            remaining -= stackSize;
        }
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 0.6F, 1.2F);
    }

    private void depositItem(@NotNull ItemStack item) {
        storage.addItem(item, item.getAmount());
        getPlayer().playSound(getPlayer(), Sound.UI_BUTTON_CLICK, 0.6F, 1.3F);
        renderPage();
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) return;

        if (!Objects.equals(e.getView().getTopInventory(), inventory)) return;

        int slot = e.getSlot();

        if (Objects.equals(clickedInventory, inventory)) {
            if (DEPOSIT_SLOTS.contains(slot)) {
                return;
            }

            e.setCancelled(true);

            if (slot == 36 && page > 0) {
                page--;
                renderPage();
                getPlayer().playSound(getPlayer(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                return;
            }

            if (slot == 44 && hasNextPage) {
                page++;
                renderPage();
                getPlayer().playSound(getPlayer(), Sound.UI_BUTTON_CLICK, 1F, 1.1F);
                return;
            }

            if (slot == 43) {
                getPlayer().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                return;
            }

            ItemStack key = slotKeyMap.get(slot);
            if (key == null) return;

            boolean withdrawAll = e.isShiftClick();
            long amount;
            if (withdrawAll) {
                amount = storage.getAmount(key);
            } else if (e.isRightClick()) {
                amount = 1;
            } else {
                amount = Math.min(64, storage.getAmount(key));
            }

            if (amount <= 0) return;

            withdrawFromStorage(key, amount);
            return;
        }

        if (Objects.equals(clickedInventory, e.getView().getBottomInventory()) && e.isShiftClick()) {
            ItemStack current = e.getCurrentItem();
            if (current == null || current.getType() == Material.AIR) return;

            e.setCancelled(true);
            depositItem(current);
            e.setCurrentItem(null);
        }
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
        for (int slot : DEPOSIT_SLOTS) {
            ItemStack deposit = inventory.getItem(slot);
            if (deposit == null || deposit.getType() == Material.AIR) continue;

            storage.addItem(deposit, deposit.getAmount());
            inventory.setItem(slot, null);
        }
    }
}
