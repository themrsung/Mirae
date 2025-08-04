package com.themrsung.mirae.command.admin;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.command.MiraeCommand;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Enchant command.
 */
public class EnchantCommand extends MiraeCommand {
    /**
     * Creates a new command.
     */
    public EnchantCommand() {
        super("enchant");
        setAliases(List.of(
                "enchantment"
        ));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("/enchant 인챈트명 레벨").style(MX.STYLE_WARNING));
            return false;
        } else if (args.length == 1) {
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.AIR) {
                sender.sendMessage(Component.text("손에 든 아이템이 없습니다.").style(MX.STYLE_WARNING));
                return false;
            }

            ItemMeta meta = item.getItemMeta();

            switch (args[0].toLowerCase()) {
                case "unbreakable" -> {
                    if (meta != null && !item.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE)) {
                        meta.setUnbreakable(true);
                        item.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    }
                }

                case "breakable" -> {
                    if (meta != null) {
                        meta.setUnbreakable(false);
                        item.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    }
                }

                default -> {
                    sender.sendMessage(Component.text("/enchant 인챈트명 레벨").style(MX.STYLE_WARNING));
                    return false;
                }
            }

            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);

            sender.sendMessage(Component.text("인챈트가 적용되었습니다.").style(MX.STYLE_GOOD));
            return true;
        }

        Enchantment enchantment;

        try {
            enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(NamespacedKey.minecraft(args[0]));
        } catch (Throwable e) {
            sender.sendMessage(Component.text("인챈트명을 확인해주세요.").style(MX.STYLE_WARNING));
            return false;
        }

        int level;

        try {
            level = Integer.parseInt(args[1]);
            if (level > 255) throw new NumberFormatException("Too high");
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("레벨을 확인해주세요.").style(MX.STYLE_WARNING));
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            sender.sendMessage(Component.text("손에 든 아이템이 없습니다.").style(MX.STYLE_WARNING));
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (level <= 0) {
            if (meta != null && meta.hasEnchant(enchantment)) {
                meta.removeEnchant(enchantment);

                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);

                sender.sendMessage(Component.text("인챈트가 제거되었습니다.").style(MX.STYLE_GOOD));
                return true;
            } else {
                sender.sendMessage(Component.text("해당 인챈트가 존재하지 않습니다.").style(MX.STYLE_WARNING));
                return false;
            }
        } else {
            if (meta != null) {
                meta.addEnchant(enchantment, level, true);
            }
        }

        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);

        sender.sendMessage(Component.text("인챈트가 적용되었습니다.").style(MX.STYLE_GOOD));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> {
                List<String> enchantments = new ArrayList<>();

                enchantments.add("unbreakable");
                enchantments.add("breakable");
                yield enchantments;
            }
            case 2 -> List.of("레벨을 입력하세요. (인챈트 제거: -1)");
            default -> List.of();
        };
    }
}
