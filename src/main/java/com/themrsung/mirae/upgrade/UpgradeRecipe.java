package com.themrsung.mirae.upgrade;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.skill.SkillType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An upgrade recipe.
 */
@FunctionalInterface
public interface UpgradeRecipe {
    /**
     * Performs the upgrade.
     *
     * @param left   The left item
     * @param right  The right item
     * @param ticket The upgrade ticket
     * @return The upgraded item
     */
    @Nullable ItemStack upgrade(@NotNull ItemStack left, @NotNull ItemStack right, @Nullable ItemStack ticket);

    /**
     * The default probability of success.
     */
    double DEFAULT_SUCCESS_RATE = 1d;

    /**
     * Imprint for success ticket users.
     */
    @NotNull Component SUCCESS_TICKET_IMPRINT = Component.text("이 아이템은 강화 성공 티켓을 이용해 생성되었습니다.").style(MX.STYLE_NORMAL);

    /**
     * Returns the default probability of success.
     *
     * @return The default probability of success
     */
    default double getSuccessRate() {
        return DEFAULT_SUCCESS_RATE;
    }

    /**
     * Returns the probability of success for the given account.
     *
     * @param account The account
     * @return The probability of success
     */
    default double getSuccessRateFor(@Nullable Account account) {
        if (account == null) {
            return getSuccessRate();
        } else {
            return Upgrades.getSuccessRateForSkillLevel(
                    getSuccessRate(),
                    account.getSkillLevel(SkillType.UPGRADING)
            );
        }
    }

}
