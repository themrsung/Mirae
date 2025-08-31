package com.themrsung.mirae.gui.donor;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.economy.EconomyCause;
import com.themrsung.mirae.economy.EconomyResult;
import com.themrsung.mirae.gui.AbstractGUI;
import com.themrsung.mirae.item.lootbox.LootBox;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Menu for donors.
 */
public class DonorMenu extends AbstractGUI {
    /**
     * Returns the price to unlock the {@code n}th extra home.
     *
     * @param n The number
     * @return The price
     */
    public static long getExtraHomePrice(int n) {
        if (n <= 10) {
            return 1;
        } else {
            return n - 9;
        }
    }

    /**
     * Random player head price.
     */
    public static final long RANDOM_PLAYER_HEAD_PRICE = 3;

    /**
     * Recent days.
     */
    public static final int RANDOM_PLAYER_RECENT_DAYS = 5;

    /**
     * Random title price.
     */
    public static final long RANDOM_TITLE_PRICE = 1;

    /**
     * Creates a new menu.
     *
     * @param player The player
     */
    public DonorMenu(@NotNull Player player) {
        super(player, 27, Component.text("후원자 메뉴").style(MX.STYLE_GOOD));

        this.callbacks = new HashMap<>();
        this.account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        render();
    }

    private final @NotNull Account account;
    private final @NotNull Map<Integer, Runnable> callbacks;

    private void render() {
        inventory.clear();
        callbacks.clear();

        /// Donor info
        CustomStack medalStack = CustomStack.getInstance("iageneric:donator_medal");
        if (medalStack != null) {
            ItemStack medalItem = medalStack.getItemStack();

            if (medalItem != null) {
                ItemMeta meta = medalItem.getItemMeta();
                meta.displayName(Component.text("후원해주셔서 감사합니다.").style(MX.STYLE_GOOD));
                meta.lore(List.of(
                        Component.text("후원코인 잔액: ").style(MX.STYLE_NORMAL)
                                .append(Component.text(MX.formatCoinBalance(account.getCoinBalance()))).style(MX.STYLE_SPECIAL)
                ));

                medalItem.setItemMeta(meta);
            }

            inventory.setItem(0, medalItem);
            callbacks.put(0, () -> player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1));
        }

        /// EXTRA HOME
        ItemStack buyHome = new ItemStack(Material.WHITE_STAINED_GLASS);
        ItemMeta homeMeta = buyHome.getItemMeta();

        int n = account.getMaxExtraHomes() + 1;
        long price = getExtraHomePrice(n);

        homeMeta.displayName(Component.text("추가 홈 구매").style(MX.STYLE_SPECIAL));
        homeMeta.lore(List.of(
                Component.text("추가 홈을 구입합니다.").style(MX.STYLE_NORMAL),
                Component.text("  - 현재 한도: ").style(MX.STYLE_NORMAL)
                        .append(Component.text(account.getMaxExtraHomes() + "개").style(MX.STYLE_SPECIAL)),
                Component.text("  - 다음 홈 가격: ").style(MX.STYLE_NORMAL)
                        .append(Component.text(MX.formatCoinBalance(price)).style(MX.STYLE_SPECIAL))
        ));

        buyHome.setItemMeta(homeMeta);
        inventory.setItem(2, buyHome);

        callbacks.put(2, () -> {
            long balance = account.getCoinBalance();

            if (balance < price) {
                player.sendMessage(Component.text("후원 코인이 부족합니다.").style(MX.STYLE_ERROR));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            EconomyResult result = Mirae.getState()
                    .withdrawCoinBalance(account, price, EconomyCause.DONOR_SHOP_BUY, "Bought " + n + "th extra home.");

            if (!result.isSuccess()) {
                player.sendMessage(Component.text("오류가 발생했습니다.").style(MX.STYLE_ERROR));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            account.setMaxExtraHomes(n);

            player.sendMessage(Component.text("추가 홈을 구입하였습니다!").style(MX.STYLE_GOOD));
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });

        /// RANDOM PLAYER HEAD
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();

        headMeta.setOwningPlayer(player);
        headMeta.displayName(Component.text("랜덤 머리").style(MX.STYLE_GOOD));
        headMeta.lore(List.of(
                Component.text("최근 " + RANDOM_PLAYER_RECENT_DAYS + "일간 접속한 플레이어 중 랜덤으로 머리를 획득합니다.").style(MX.STYLE_NORMAL),
                Component.text("  - 가격: ").style(MX.STYLE_NORMAL)
                        .append(Component.text(MX.formatCoinBalance(RANDOM_PLAYER_HEAD_PRICE)).style(MX.STYLE_SPECIAL))
        ));

        playerHead.setItemMeta(headMeta);
        inventory.setItem(3, playerHead);
        callbacks.put(3, () -> {
            long balance = account.getCoinBalance();

            if (balance < RANDOM_PLAYER_HEAD_PRICE) {
                player.sendMessage(Component.text("후원 코인이 부족합니다.").style(MX.STYLE_ERROR));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            LocalDateTime cutoff = LocalDateTime.now().minusDays(RANDOM_PLAYER_RECENT_DAYS);

            Set<UUID> playerIds = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toSet());
            Mirae.getState().getAccounts().forEach(a -> {
                LocalDateTime lastSeen = a.getLastSeenTime();
                if (lastSeen == null || lastSeen.isBefore(cutoff)) return;

                playerIds.add(a.getUniqueId());
            });

            List<UUID> playerIdList = new ArrayList<>(playerIds);
            Collections.shuffle(playerIdList);

            OfflinePlayer target = Bukkit.getOfflinePlayer(playerIdList.getFirst());
            Account targetAccount = MX.requireAccountNonNull(Mirae.getState().getAccount(target));

            String targetName = targetAccount.getName();

            EconomyResult result = Mirae.getState().withdrawCoinBalance(account, RANDOM_PLAYER_HEAD_PRICE, EconomyCause.DONOR_SHOP_BUY, "Bought random player head.");
            if (!result.isSuccess()) {
                player.sendMessage(Component.text("오류가 발생했습니다."));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " minecraft:player_head[minecraft:profile=" + targetName + "]");

            player.sendMessage(targetAccount.getDisplayName(MX.STYLE_SPECIAL)
                    .append(Component.text("님의 머리를 획득했습니다.").style(MX.STYLE_NORMAL)));
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });

        /// RANDOM TITLE BOX
        ItemStack randomTitle = new ItemStack(Material.BOOK);
        ItemMeta titleMeta = randomTitle.getItemMeta();

        titleMeta.displayName(Component.text("랜덤 칭호").style(MX.STYLE_GOOD));
        titleMeta.lore(List.of(
                Component.text("총 " + AccountTitle.getAcquirableTitles().size() + "개의 칭호 중 하나를 받을 수 있는 랜덤 박스를 획득합니다.").style(MX.STYLE_NORMAL),
                Component.text("  - 가격: ").style(MX.STYLE_NORMAL)
                        .append(Component.text(MX.formatCoinBalance(RANDOM_TITLE_PRICE)).style(MX.STYLE_SPECIAL))
        ));

        randomTitle.setItemMeta(titleMeta);
        inventory.setItem(4, randomTitle);
        callbacks.put(4, () -> {
            long balance = account.getCoinBalance();

            if (balance < RANDOM_TITLE_PRICE) {
                player.sendMessage(Component.text("후원 코인이 부족합니다.").style(MX.STYLE_ERROR));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            EconomyResult result = Mirae.getState().withdrawCoinBalance(account, RANDOM_TITLE_PRICE, EconomyCause.DONOR_SHOP_BUY, "Bought random title box.");
            if (!result.isSuccess()) {
                player.sendMessage(Component.text("오류가 발생했습니다."));
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                return;
            }

            LootBox emojiBox = LootBox.TITLE_BOX;
            ItemStack boxItem = emojiBox.getItem();

            MX.giveItems(player, boxItem);

            player.sendMessage(Component.text("랜덤 칭호 박스를 획득했습니다!").style(MX.STYLE_GOOD));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        });
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        if (!Objects.equals(inventory, e.getClickedInventory())) return;
        e.setCancelled(true);

        Runnable callback = callbacks.get(e.getSlot());
        if (callback == null) return;

        callback.run();

        render();
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {
    }
}
