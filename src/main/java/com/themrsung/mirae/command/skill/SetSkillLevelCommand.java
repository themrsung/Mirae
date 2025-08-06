package com.themrsung.mirae.command.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Set skill level command.
 */
public class SetSkillLevelCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SetSkillLevelCommand() {
        super("setskilllevel");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(Component.text("/setskilllevel 대상 스킬 레벨").style(MX.STYLE_WARNING));
            return false;
        }

        String query = args[0];

        Account account = Mirae.getState().getAccounts().stream()
                .filter(a -> a.getName().equalsIgnoreCase(query))
                .findAny()
                .orElse(null);

        if (account == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        String rawType = args[1].toUpperCase();
        SkillType type;

        try {
            type = SkillType.valueOf(rawType);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("올바르지 않은 스킬입니다.").style(MX.STYLE_WARNING));
            return false;
        }

        long level;

        try {
            level = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(INVALID_NUMBER);
            return false;
        }

        account.setSkillLevel(type, level);
        sender.sendMessage(account.getDisplayName(MX.STYLE_SPECIAL)
                .append(Component.text("님의 ").style(MX.STYLE_NORMAL))
                .append(type.getDisplayName())
                .append(Component.text(" 레벨을 ").style(MX.STYLE_NORMAL))
                .append(Component.text(level).style(MX.STYLE_SPECIAL))
                .append(Component.text("로 설정했습니다.").style(MX.STYLE_NORMAL)));

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if (!sender.isOp()) return List.of();

        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
            case 2 -> Arrays.stream(SkillType.values())
                    .map(SkillType::toString)
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .toList();
            case 3 -> {
                String query = args[0];
                Account account = Mirae.getState().getAccounts().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(query))
                        .findAny()
                        .orElse(null);

                if (account == null) {
                    yield List.of("계정을 찾을 수 없습니다.");
                }

                SkillType type;

                try {
                    type = SkillType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    yield List.of("스킬을 찾을 수 없습니다.");
                }

                yield List.of(Long.toString(account.getSkillLevel(type)));
            }
            default -> List.of();
        };
    }
}
