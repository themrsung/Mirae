package com.themrsung.mirae.listener.player;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.event.economy.EconomyCause;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Player related event listener.
 */
public final class PlayerListener implements Listener {
    public static final double STARTING_BALANCE = 0;
    public static final long STARTING_COIN_BALANCE = 0;

    /**
     * Teleports of distance less than this value will not be logged.
     */
    public static final double TELEPORT_LOG_IGNORE_DISTANCE = 25;

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
        } else {
            // Rejoin
            account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));
            List<Component> mailList = account.getMailList();

            if (!mailList.isEmpty()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Mirae.getInstance(), () -> {
                    mailList.forEach(player::sendMessage);
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }, 100);
            }
        }

        Mirae.getState().logAccountActivity(account);

        Bukkit.broadcast(Component.text("[").style(MX.STYLE_NORMAL)
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

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

        Mirae.getState().logAccountActivity(account);

        Component message = renderChat(account, ((TextComponent) e.originalMessage()).content());
        e.setCancelled(true);

        if (account.isMuted()) {
            player.sendMessage(message);
        } else {
            Bukkit.getOnlinePlayers().forEach(p -> {
                Account a = MX.requireAccountNonNull(Mirae.getState().getAccount(p));
                if (a.isIgnoringAccount(account) || account.isIgnoringAccount(a)) return;

                p.sendMessage(message);
            });
        }
    }

    private @NotNull Component renderChat(Account sender, String message) {
        return sender.getTier().getDisplayName()
                .append(Component.text(" ").style(MX.STYLE_NORMAL))
                .append(sender.getCurrentTitle().getValue())
                .append(Component.text(" ").style(MX.STYLE_NORMAL))
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
        if (Objects.equals(from.getWorld(), to.getWorld()) && from.distanceSquared(to) >= Math.pow(TELEPORT_LOG_IGNORE_DISTANCE, 2)) {
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

        Player player = e.getPlayer();
        Account account = MX.requireAccountNonNull(Mirae.getState().getAccount(player));

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

        Bukkit.broadcast(Component.text("[").style(MX.STYLE_NORMAL)
                .append(Component.text("-").style(MX.STYLE_ERROR))
                .append(Component.text("] ").style(MX.STYLE_NORMAL))
                .append(account.getDisplayName(MX.STYLE_SPECIAL)));
    }
}
