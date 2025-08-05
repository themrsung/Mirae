package com.themrsung.mirae.gui.upgrade;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.skill.SkillType;
import com.themrsung.mirae.upgrade.UpgradeRecipe;
import com.themrsung.mirae.upgrade.Upgrades;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Sword upgrade menu.
 */
public class UpgradeMenu extends AbstractUpgradeMenu {
    /**
     * The threshold for broadcast.
     */
    public static final double SUCCESS_BROADCAST_RATE_THRESHOLD = 0.1;

    /**
     * Creates a new menu.
     *
     * @param player The player
     */
    public UpgradeMenu(@NotNull Player player) {
        super(player, Component.text("강화").style(Style.style()
                .color(TextColor.fromHexString("#06068c"))
                .decorate(TextDecoration.BOLD)
                .build()));
    }

    @Override
    protected void renderConfirmButton() {
        ItemStack left = getLeftItem();
        ItemStack right = getRightItem();

        ItemStack button;

        if (left == null || right == null) {
            button = getCancelButton();
        } else if (Upgrades.getRecipes().stream().anyMatch(r -> r.upgrade(left, right, getTicketItem()) != null)) {
            button = getConfirmButton();
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        } else {
            button = getCancelButton();
        }

        inventory.setItem(SLOT_CONFIRM, button);
    }

    @Override
    protected void onLeftSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onRightSlotClick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onTicketSlotSlick(@NotNull InventoryClickEvent e) {

    }

    @Override
    protected void onUpgradeConfirm() {
        ItemStack left = getLeftItem();
        ItemStack right = getRightItem();

        if (left == null || right == null) {
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }

        Set<UpgradeRecipe> recipes = Upgrades.getRecipes();
        for (UpgradeRecipe recipe : recipes) {
            ItemStack ticket = getTicketItem();
            ItemStack result = recipe.upgrade(left, right, ticket);

            if (result == null) continue;

            double baseRate = recipe.getSuccessRateFor(account);
            double finalRate = CustomItem.UPGRADE_SUCCESS_TICKET.isItem(ticket) ? 1 : baseRate;

            Random random = new Random();
            double random1 = random.nextDouble() + Double.MIN_VALUE;

            boolean success = finalRate >= 1 || random1 < finalRate;

            if (success) {
                player.sendMessage(UPGRADE_SUCCESS_MESSAGE);
                player.playSound(player, UPGRADE_SUCCESS_SOUND, 1, 1);

                inventory.setItem(SLOT_LEFT, result);

                if (finalRate <= SUCCESS_BROADCAST_RATE_THRESHOLD) {
                    ItemMeta meta = result.getItemMeta();
                    Component displayName = Objects.requireNonNullElse(meta.displayName(), Component.text(MX.getKoreanMaterialName(result.getType())).style(MX.STYLE_SPECIAL));

                    double finalRatePercent = (double) Math.round(finalRate * 10000) / 100;

                    Bukkit.broadcast(account.getDisplayName(MX.STYLE_SPECIAL)
                            .append(Component.text("님이 ").style(MX.STYLE_NORMAL))
                            .append(Component.text(finalRatePercent + "%").style(MX.STYLE_WARNING))
                            .append(Component.text("의 확률을 뚫고 [").style(MX.STYLE_NORMAL))
                            .append(displayName.hoverEvent(result.asHoverEvent()))
                            .append(Component.text("]를 획득했습니다.!").style(MX.STYLE_NORMAL)));
                }
            } else {
                player.sendMessage(UPGRADE_FAIL_MESSAGE);
                player.playSound(player, UPGRADE_FAIL_SOUND, 1, 1);
            }

            inventory.setItem(SLOT_RIGHT, null);

            if (baseRate < 1 && ticket != null) {
                inventory.setItem(SLOT_TICKET, null);

                double random2 = random.nextDouble();
                if (random2 < 0.01) {
                    account.incrementLevel(SkillType.UPGRADING);
                }
            }

            return;
        }
    }
}
