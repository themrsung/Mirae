package com.themrsung.mirae.gui.convenience;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTier;
import com.themrsung.mirae.gui.AbstractGUI;
import com.themrsung.mirae.gui.donor.DonorMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Main Menu (Shift + F)
 */
public class MainMenu extends AbstractGUI {
    /**
     * The Minelist URL.
     */
    public static final @NotNull String MINELIST_URL = "https://minelist.kr/servers/16551-themrsung.com/votes/new";

    /**
     * The Discord URL.
     */
    public static final @NotNull String DISCORD_URL = "https://discord.gg/yuSj6SPDaH";

    /**
     * Creates a new menu.
     *
     * @param player The player
     */
    public MainMenu(@NotNull Player player) {
        super(player, 27, Component.text("미래 서버").style(Style.style()
                .color(TextColor.fromHexString("#ff2e01"))
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
                .build()));

        this.account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
        this.callbacks = new HashMap<>();

        initializeWarps();
        initializeHomes();
        initializeLinks();
        initializeConveniences();
    }

    private void initializeWarps() {
        /// SPAWN
        ItemStack spawn = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta spawnMeta = spawn.getItemMeta();

        spawnMeta.displayName(Component.text("스폰").style(MX.STYLE_GOOD));

        spawn.setItemMeta(spawnMeta);
        inventory.setItem(0, spawn);
        callbacks.put(0, () -> {
            Bukkit.dispatchCommand(player, "spawn");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// WILDERNESS
        ItemStack wilderness = new ItemStack(Material.DIRT);
        ItemMeta wildernessMeta = wilderness.getItemMeta();

        wildernessMeta.displayName(Component.text("야생").style(MX.STYLE_NORMAL));

        wilderness.setItemMeta(wildernessMeta);
        inventory.setItem(1, wilderness);
        callbacks.put(1, () -> {
            Bukkit.dispatchCommand(player, "warp wild");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// SHOPS
        ItemStack shops = new ItemStack(Material.RED_CONCRETE);
        ItemMeta shopsMeta = shops.getItemMeta();

        shopsMeta.displayName(Component.text("상점").style(MX.STYLE_BUY));

        shops.setItemMeta(shopsMeta);
        inventory.setItem(2, shops);
        callbacks.put(2, () -> {
            Bukkit.dispatchCommand(player, "warp shop");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// BACK (RECENT)
        ItemStack back = getBackButton();
        ItemMeta backMeta = back.getItemMeta();

        backMeta.displayName(Component.text("최근 위치로").style(MX.STYLE_NORMAL));
        backMeta.lore(List.of(Component.text("최근에 텔레포트한 위치로 이동합니다.").style(MX.STYLE_NORMAL)));

        back.setItemMeta(backMeta);
        inventory.setItem(7, back);
        callbacks.put(7, () -> {

            Bukkit.dispatchCommand(player, "back");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// BACK (DEATH)
        ItemStack death = getCancelButton();
        ItemMeta deathMeta = death.getItemMeta();

        deathMeta.displayName(Component.text("죽은 위치로").style(MX.STYLE_ERROR));
        deathMeta.lore(List.of(Component.text("최근에 사망한 위치로 이동합니다.").style(MX.STYLE_NORMAL)));

        death.setItemMeta(deathMeta);
        inventory.setItem(8, death);
        callbacks.put(8, () -> {
            Bukkit.dispatchCommand(player, "back death");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });
    }

    private void initializeHomes() {
        /// MAIN HOME
        Location mainHome = account.getHome();

        ItemStack home = new ItemStack(Material.OAK_WOOD);
        ItemMeta homeMeta = home.getItemMeta();

        homeMeta.displayName(Component.text("집").style(MX.STYLE_NORMAL));
        if (mainHome != null) {
            homeMeta.lore(List.of(
                    Component.text("world: \"" + mainHome.getWorld().getName() + "\"").style(MX.STYLE_NORMAL),
                    Component.text("x: " + Math.round(mainHome.getX())).style(MX.STYLE_NORMAL),
                    Component.text("y: " + Math.round(mainHome.getY())).style(MX.STYLE_NORMAL),
                    Component.text("z: " + Math.round(mainHome.getZ())).style(MX.STYLE_NORMAL)
            ));
        } else {
            homeMeta.lore(List.of(Component.text("홈이 설정되지 않았습니다.").style(MX.STYLE_WARNING)));
        }

        home.setItemMeta(homeMeta);
        inventory.setItem(9, home);
        callbacks.put(9, () -> {
            Bukkit.dispatchCommand(player, "home");
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// EXTRA HOMES
        List<String> extraHomeKeys = account.getExtraHomeMap().keySet().stream()
                .sorted()
                .toList();

        int q = 2;
        for (int i = 0; i < Math.min(extraHomeKeys.size(), 5); i++) {
            String key = extraHomeKeys.get(i);
            Location value = account.getExtraHome(key);

            if (value == null) continue;

            ItemStack extraHome = new ItemStack(Material.OAK_WOOD, q++);
            ItemMeta extraHomeMeta = extraHome.getItemMeta();

            extraHomeMeta.displayName(Component.text(key).style(MX.STYLE_NORMAL));
            extraHomeMeta.lore(List.of(
                    Component.text("world: \"" + value.getWorld().getName() + "\"").style(MX.STYLE_NORMAL),
                    Component.text("x: " + Math.round(value.getX())).style(MX.STYLE_NORMAL),
                    Component.text("y: " + Math.round(value.getY())).style(MX.STYLE_NORMAL),
                    Component.text("z: " + Math.round(value.getZ())).style(MX.STYLE_NORMAL)
            ));

            int slot = i + 10;

            extraHome.setItemMeta(extraHomeMeta);
            inventory.setItem(slot, extraHome);
            callbacks.put(slot, () -> {
                Bukkit.dispatchCommand(player, "home " + key);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            });
        }
    }

    private void initializeLinks() {
        /// DONOR SHOP
        ItemStack donorShop = new ItemStack(Material.NETHER_STAR);
        ItemMeta donorShopMeta = donorShop.getItemMeta();

        donorShopMeta.displayName(Component.text("후원 상점").style(MX.STYLE_SELL));
        donorShopMeta.lore(List.of(
                Component.text("후원 코인을 사용할 수 있습니다.").style(MX.STYLE_NORMAL)
        ));

        donorShop.setItemMeta(donorShopMeta);
        inventory.setItem(17, donorShop);
        callbacks.put(17, () -> {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            DonorMenu menu = new DonorMenu(player);
            menu.openGUI();
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
        });

        /// MINELIST
        ItemStack minelist = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta minelistMeta = minelist.getItemMeta();

        minelistMeta.displayName(Component.text("마인리스트").style(MX.STYLE_GOOD));

        minelist.setItemMeta(minelistMeta);
        inventory.setItem(25, minelist);
        callbacks.put(25, () -> {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.sendMessage(Component.text("마인리스트 추천하기: ").style(MX.STYLE_NORMAL)
                    .append(Component.text("[클릭]").style(MX.STYLE_SPECIAL)
                            .clickEvent(ClickEvent.openUrl(MINELIST_URL))));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        });

        /// DISCORD
        ItemStack discord = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta discordMeta = discord.getItemMeta();

        discordMeta.displayName(Component.text("디스코드").style(MX.STYLE_SPECIAL));

        discord.setItemMeta(discordMeta);
        inventory.setItem(26, discord);
        callbacks.put(26, () -> {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.sendMessage(Component.text("디스코드 가입하기: ").style(MX.STYLE_NORMAL)
                    .append(Component.text("[클릭]").style(MX.STYLE_SPECIAL)
                            .clickEvent(ClickEvent.openUrl(DISCORD_URL))));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        });
    }

    private void initializeConveniences() {
        if (account.getTier().isAtLeast(AccountTier.GREEN)) {
            ItemStack workbench = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta meta = workbench.getItemMeta();

            meta.displayName(Component.text("작업대").style(MX.STYLE_NORMAL));

            workbench.setItemMeta(meta);
            inventory.setItem(18, workbench);
            callbacks.put(18, () -> {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                new VirtualWorkbench(player).openGUI();
            });
        } else {
            ItemStack unlockedAtGreen = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = unlockedAtGreen.getItemMeta();

            meta.displayName(Component.text("사용 불가").style(MX.STYLE_ERROR));
            meta.lore(List.of(Component.text("그린 등급에서 해제됩니다.").style(MX.STYLE_NORMAL)));

            unlockedAtGreen.setItemMeta(meta);
            inventory.setItem(18, unlockedAtGreen);
            callbacks.put(18, () -> player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1));
        }

        if (account.getTier().isAtLeast(AccountTier.GOLD)) {
            ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
            ItemMeta meta = enderChest.getItemMeta();

            meta.displayName(Component.text("엔더 상자").style(MX.STYLE_NORMAL));

            enderChest.setItemMeta(meta);
            inventory.setItem(19, enderChest);
            callbacks.put(19, () -> {
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
                new VirtualEnderChest(player).openGUI();
            });
        } else {
            ItemStack unlockedAtGold = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = unlockedAtGold.getItemMeta();

            meta.displayName(Component.text("사용 불가").style(MX.STYLE_ERROR));
            meta.lore(List.of(Component.text("골드 등급에서 해제됩니다.").style(MX.STYLE_NORMAL)));

            unlockedAtGold.setItemMeta(meta);
            inventory.setItem(19, unlockedAtGold);
            callbacks.put(19, () -> player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1));
        }
    }

    private final @NotNull Account account;
    private final @NotNull Map<Integer, Runnable> callbacks;

    @Override
    protected void onClick(@NotNull InventoryClickEvent e) {
        if (!Objects.equals(inventory, e.getClickedInventory())) return;
        e.setCancelled(true);

        int slot = e.getSlot();
        Runnable callback = callbacks.get(slot);

        if (callback == null) return;

        callback.run();
    }

    @Override
    protected void onClose(@NotNull InventoryCloseEvent e) {

    }
}
