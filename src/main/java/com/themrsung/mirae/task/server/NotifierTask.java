package com.themrsung.mirae.task.server;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.util.Carousel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Server notifier task.
 */
public class NotifierTask implements Runnable {
    public static final @NotNull Component PREFIX = Component.text("[").style(MX.STYLE_NORMAL)
            .append(Component.text("!").style(MX.STYLE_WARNING))
            .append(Component.text("] ").style(MX.STYLE_NORMAL));


    /**
     * Creates a new task.
     */
    public NotifierTask() {
        this.messages = Carousel.of(
                PREFIX.append(Component.text("디스코드에서 공지를 확인하고 보이스챗에 참여하세요! ").style(MX.STYLE_NORMAL)
                        .append(Component.text("[클릭]").style(MX.STYLE_SPECIAL)
                                .hoverEvent(HoverEvent.showText(Component.text("디스코드에 가입합니다...").style(MX.STYLE_NORMAL)))
                                .clickEvent(ClickEvent.openUrl("https://discord.gg/yuSj6SPDaH")))),

                PREFIX.append(Component.text("스코어보드를 숨길 수 있습니다! ").style(MX.STYLE_NORMAL)
                        .append(Component.text("[클릭]").style(MX.STYLE_SPECIAL)
                                .hoverEvent(HoverEvent.showText(Component.text("/sb").style(MX.STYLE_NORMAL)))
                                .clickEvent(ClickEvent.runCommand("/sb")))),

                PREFIX.append(Component.text("지역채팅을 사용할 수 있습니다.").style(MX.STYLE_NORMAL)
                        .append(Component.text("[클릭]")
                                .hoverEvent(HoverEvent.showText(Component.text("/l").style(MX.STYLE_WARNING)))
                                .clickEvent(ClickEvent.runCommand("/localchat"))))
        );


    }

    private final @NotNull Carousel<Component> messages;

    @Override
    public void run() {
        Bukkit.broadcast(messages.getNext());
    }
}
