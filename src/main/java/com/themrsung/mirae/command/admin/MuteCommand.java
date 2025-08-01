package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class MuteCommand extends MiraeCommand {
    public MuteCommand() {
        super("mute");
        setAliases(List.of(
                "뮤트"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/mute 대상").style(MX.STYLE_WARNING));
            return false;
        }

        Account target = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);

        if (target == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        if (args.length < 2 || args[1].equalsIgnoreCase("toggle")) {
            boolean muted = target.isMuted();
            target.setMuted(!muted);

            sender.sendMessage(target.getDisplayName(MX.STYLE_SPECIAL)
                    .append(Component.text("님의 뮤트 상태를 변경했습니다. 현재 상태: ").style(MX.STYLE_NORMAL))
                    .append(!muted ? Component.text("뮤트 중").style(MX.STYLE_ERROR) : Component.text("정상").style(MX.STYLE_GOOD)));
            return true;
        }

        boolean shouldMute = !args[1].equalsIgnoreCase("false");
        String muteMinutes = args.length > 2 ? args[2] : "-1";
        long minutes;

        try {
            minutes = Long.parseLong(muteMinutes);
        } catch (NumberFormatException e) {
            sender.sendMessage(INVALID_NUMBER);
            return false;
        }

        LocalDateTime expiration = minutes > 0 ? LocalDateTime.now().plusMinutes(minutes) : null;

        target.setMuted(shouldMute, expiration);

        sender.sendMessage(target.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님의 뮤트 상태를 변경했습니다. 현재 상태: ").style(MX.STYLE_NORMAL))
                .append(shouldMute ? Component.text("뮤트 중").style(MX.STYLE_ERROR) : Component.text("정상").style(MX.STYLE_GOOD)));

        if (minutes > 0) {
            sender.sendMessage(Component.text("뮤트는 " + minutes + "분 뒤 해제됩니다.").style(MX.STYLE_NORMAL));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> List.of("true", "false", "toggle");
            case 3 -> List.of("뮤트 시간 (분)");
            default -> List.of();
        };
    }
}
