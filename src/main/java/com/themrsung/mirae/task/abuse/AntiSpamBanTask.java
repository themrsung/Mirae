package com.themrsung.mirae.task.abuse;


import com.themrsung.mirae.MX;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.listener.abuse.AntiSpamListener;
import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * Anti-spam ban task.
 */
public class AntiSpamBanTask implements Runnable {
    public static final @NotNull String BAN_MESSAGE = "계정이 영구정지되었습니다. 관리자에게 문의하세요. 디스코드: @themrsung";

    @Override
    public void run() {
        AntiSpamListener listener = Listeners.ANTI_SPAM_LISTENER;

        listener.forEachCommand((id, count) -> banSpammer(id, count, 15));
        listener.forEachChat((id, count) -> banSpammer(id, count, 10));

        listener.clearSpamCounts();
    }

    protected void banSpammer(@NotNull UUID id, int count, int threshold) {
        if (count < threshold) return;

        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        ProfileBanList banList = Bukkit.getServer().getBanList(BanListType.PROFILE);
        banList.addBan(player.getPlayerProfile(), BAN_MESSAGE, (Date) null, BAN_MESSAGE);

        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer != null)
            onlinePlayer.kick(Component.text(BAN_MESSAGE).style(MX.STYLE_ERROR));
    }
}
