package com.themrsung.mirae.social;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.account.Account;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Direct content.
 *
 * @param uniqueId  The unique identifier
 * @param time      The time
 * @param sender    The sender
 * @param recipient The recipient
 * @param content   The content
 */
public record DirectMessage(
        @NotNull UUID uniqueId,
        @NotNull LocalDateTime time,
        @NotNull Account sender,
        @NotNull Account recipient,
        @NotNull Component content
) {
    /**
     * Composes a new direct message.
     *
     * @param sender    The sender
     * @param recipient The recipient
     * @param content   The content
     * @return The message
     */
    public static @NotNull DirectMessage compose(@NotNull Account sender, @NotNull Account recipient, @NotNull Component content) {
        return new DirectMessage(UUID.randomUUID(), LocalDateTime.now(), sender, recipient, content);
    }

    /**
     * Returns the message as shown to console.
     *
     * @return Console message
     */
    public @NotNull Component asShownToConsole() {
        String senderName = sender.getName();
        String recipientName = recipient.getName();

        String prefix = "[" + senderName + " -> " + recipientName + "] ";
        return Component.text(prefix).append(content);
    }

    /**
     * Returns the message as shown to a third party.
     *
     * @return The third party message
     */
    public @NotNull Component asShownToThirdParty() {
        Component senderDisplayName = sender.getDisplayName();
        Component recipientDisplayName = recipient.getDisplayName();

        return Component.text("[")
                .append(senderDisplayName)
                .append(Component.text(" -> "))
                .append(recipientDisplayName)
                .append(Component.text("] "))
                .append(content.applyFallbackStyle(MX.STYLE_NORMAL)
                        .hoverEvent(HoverEvent.showText(Component.text("클릭하여 복사합니다...").style(MX.STYLE_NORMAL)))
                        .clickEvent(ClickEvent.copyToClipboard(((TextComponent) content).content())));
    }

    /**
     * Returns the message as shown to a sender.
     *
     * @return The sender message
     */
    public @NotNull Component asShownToSender() {
        Component senderDisplayName = Component.text("본인");
        Component recipientDisplayName = recipient.getDisplayName();

        return Component.text("[")
                .append(senderDisplayName)
                .append(Component.text(" -> "))
                .append(recipientDisplayName)
                .append(Component.text("] "))
                .append(content
                        .hoverEvent(HoverEvent.showText(Component.text("클릭하여 복사합니다...").style(MX.STYLE_NORMAL)))
                        .clickEvent(ClickEvent.copyToClipboard(((TextComponent) content).content())));
    }

    /**
     * Returns the message as shown to a recipient.
     *
     * @return The recipient message
     */
    public @NotNull Component asShownToRecipient() {
        Component senderDisplayName = sender.getDisplayName();
        Component recipientDisplayName = Component.text("본인");

        return Component.text("[")
                .append(senderDisplayName)
                .append(Component.text(" -> "))
                .append(recipientDisplayName)
                .append(Component.text("] "))
                .append(content
                        .hoverEvent(HoverEvent.showText(Component.text("클릭하여 복사합니다...").style(MX.STYLE_NORMAL)))
                        .clickEvent(ClickEvent.copyToClipboard(((TextComponent) content).content())));
    }
}
