package com.themrsung.mirae.listener.player;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.account.AccountTier;
import com.themrsung.mirae.account.AccountTitle;
import com.themrsung.mirae.economy.EconomyCause;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Player related event listener.
 */
public class PlayerListener implements Listener {
    public static final double STARTING_BALANCE = 0;
    public static final long STARTING_COIN_BALANCE = 0;

    /**
     * Teleports of distance less than this value will not be logged.
     */
    public static final double TELEPORT_LOG_IGNORE_DISTANCE = 25;

    /**
     * Local chat distance.
     */
    public static final double LOCAL_CHAT_DISTANCE = 150;
    private static final double LOCAL_CHAT_DISTANCE_SQUARED = Math.pow(LOCAL_CHAT_DISTANCE, 2);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Account account;

        if (!Mirae.getState().hasAccount(player)) {
            // First join
            account = Account.createAccount(player.getUniqueId());
            Mirae.getState().addAccount(account);

            account.modifyBalance(STARTING_BALANCE, EconomyCause.NATIVE_DEPOSIT, "Starting balance");
            account.modifyCoinBalance(STARTING_COIN_BALANCE, EconomyCause.NATIVE_DEPOSIT, "Starting coin balance");

            giveStartingItems(player);
        } else {
            // Rejoin
            account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
            List<Component> mailList = account.getMailList();

            if (!mailList.isEmpty()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Mirae.getInstance(), () -> {
                    mailList.forEach(player::sendMessage);
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }, 100);

                account.clearMailList();
            }
        }

        Mirae.getState().logAccountActivity(account);

        e.joinMessage(Component.text("[").style(MX.STYLE_NORMAL)
                .append(Component.text("+").style(MX.STYLE_GOOD))
                .append(Component.text("] ").style(MX.STYLE_NORMAL))
                .append(account.getDisplayName(MX.STYLE_SPECIAL)));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Mirae.getState().logAccountActivity(account);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChat(AsyncChatEvent e) {
        e.setCancelled(true);

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Mirae.getState().logAccountActivity(account);

        Component message = renderChat(account, ((TextComponent) e.originalMessage()).content());

        if (account.isMuted()) {
            player.sendMessage(message);

            Bukkit.getConsoleSender().sendMessage(Component.text("[Muted] ").style(MX.STYLE_WARNING)
                    .append(message));
        } else {
            Location from = e.getPlayer().getLocation();
            boolean local = account.inLocalChat();

            Bukkit.getOnlinePlayers().forEach(p -> {
                if (local) {
                    Location to = p.getLocation();

                    if (!Objects.equals(from.getWorld().getName(), to.getWorld().getName())) return;
                    if (to.distanceSquared(from) > LOCAL_CHAT_DISTANCE_SQUARED) return;
                }

                Account a = MX.requireAccountNonNull(Mirae.getState().getAccount(p));
                if (a.isIgnoringAccount(account) || account.isIgnoringAccount(a)) return;

                p.sendMessage(message);
            });

            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    private @NotNull Component renderChat(Account sender, String message) {
        boolean local = sender.inLocalChat();

        boolean hasTier = sender.getTier() != AccountTier.DEFAULT;
        boolean hasTitle = sender.getCurrentTitle() != AccountTitle.EMPTY;

        return Component.empty()
                .append(local ? Component.text("[").style(MX.STYLE_NORMAL)
                        .append(Component.text("지역").style(MX.STYLE_WARNING)
                                .hoverEvent(HoverEvent.showText(Component.text(NumberFormat.getInstance().format(LOCAL_CHAT_DISTANCE) + "블럭 내 플레이어에게만 보여집니다.").style(MX.STYLE_NORMAL)))
                                .clickEvent(ClickEvent.runCommand("/localchat"))
                        )
                        .append(Component.text("] ").style(MX.STYLE_NORMAL)) :
                        Component.empty())
                .append(sender.getTier().getDisplayName())
                .append(Component.text(hasTier ? " " : "").style(MX.STYLE_NORMAL))
                .append(sender.getCurrentTitle().getValue())
                .append(Component.text(hasTitle ? " " : "").style(MX.STYLE_NORMAL))
                .append(sender.getDisplayName(MX.STYLE_NORMAL))
                .append(Component.text(" : ").style(MX.STYLE_NORMAL))
                .append(Component.text(message).applyFallbackStyle(MX.STYLE_NORMAL)
                        .hoverEvent(HoverEvent.showText(Component.text("클릭하여 복사합니다...").style(MX.STYLE_NORMAL)))
                        .clickEvent(ClickEvent.copyToClipboard(message)));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Mirae.getState().logAccountActivity(account);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Location from = e.getFrom();
        Location to = e.getTo();

        // Set location to go to when user types "/back"
        boolean comparable = Objects.equals(from.getWorld(), to.getWorld());
        boolean shouldLog = !comparable || from.distanceSquared(to) >= Math.pow(TELEPORT_LOG_IGNORE_DISTANCE, 2);
        if (shouldLog) {
            account.setRecentTeleportDeparture(from);
        }

        Mirae.getState().logAccountActivity(account);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setShouldDropExperience(false);
        e.getDrops().clear();

        e.setKeepInventory(true);
        e.setKeepLevel(true);

        e.setShowDeathMessages(true);

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        account.setRecentTeleportDeparture(player.getLocation());
        account.setRecentDeathLocation(player.getLocation());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Location spawn = Mirae.getState().getSpawnPoint();

        if (spawn != null) {
            e.setRespawnLocation(spawn);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        account.setLastSeenTime(LocalDateTime.now());
        account.setLastSeenLocation(player.getLocation());

        e.quitMessage(Component.text("[").style(MX.STYLE_NORMAL)
                .append(Component.text("-").style(MX.STYLE_ERROR))
                .append(Component.text("] ").style(MX.STYLE_NORMAL))
                .append(account.getDisplayName(MX.STYLE_SPECIAL)));
    }

    private static void giveStartingItems(@NotNull Player player) {
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemMeta axeMeta = axe.getItemMeta();

        axeMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        axe.setItemMeta(axeMeta);

        MX.giveItems(player.getInventory(), axe);

        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 64);
        MX.giveItems(player.getInventory(), steak);
    }
}
