package com.themrsung.mirae.command.skill;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Skill level command.
 */
public class SkillLevelCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public SkillLevelCommand() {
        super("skilllevel");
        setAliases(List.of(
                "skills",
                "skill",
                "스킬",
                "스킬레벨",
                "레벨"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        Account account;

        if (args.length > 0) {
            String query = args[0];
            account = Mirae.getState().getAccounts().stream()
                    .filter(a -> a.getName().equalsIgnoreCase(query))
                    .findAny()
                    .orElse(null);
        } else {
            account = MX.requireAccountNonNull(Mirae.getState().getAccount((Player) sender));
        }

        if (account == null) {
            sender.sendMessage(CANNOT_FIND_ACCOUNT);
            return false;
        }

        sendSkillInfo(sender, account);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> Mirae.getState().getAccounts().stream()
                    .map(Account::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
            default -> List.of();
        };
    }

    private static void sendSkillInfo(@NotNull CommandSender sender, @NotNull Account account) {
        boolean self = sender instanceof Player player && Objects.equals(player.getUniqueId(), account.getUniqueId());

        sender.sendMessage((self ? Component.text("본인").style(MX.STYLE_SPECIAL) : account.getDisplayName(MX.STYLE_SPECIAL))
                .append(Component.text("의 스킬 정보").style(MX.STYLE_NORMAL)));

        Arrays.stream(SkillType.values()).forEach(type ->
                sender.sendMessage(Component.text("  -").style(MX.STYLE_NORMAL)
                        .append(type.getDisplayName())
                        .append(Component.text(": ").style(MX.STYLE_NORMAL))
                        .append(Component.text(account.getSkillLevel(type) + "레벨")).style(MX.STYLE_SPECIAL)));
    }
}
