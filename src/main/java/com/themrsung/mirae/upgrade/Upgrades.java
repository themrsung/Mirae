package com.themrsung.mirae.upgrade;

import com.themrsung.mirae.upgrade.composition.CoalToDiamondRecipe;
import com.themrsung.mirae.upgrade.composition.IronToolToDiamondRecipe;
import com.themrsung.mirae.upgrade.composition.NetheriteToVibraniumRecipe;
import com.themrsung.mirae.upgrade.composition.WoodenToolToNetheriteRecipe;
import com.themrsung.mirae.upgrade.debug.TestUpgradeRecipe;
import com.themrsung.mirae.upgrade.enchant.CustomEnchantAcquisitionRecipe;
import com.themrsung.mirae.upgrade.enchant.CustomEnchantApplicationRecipe;
import com.themrsung.mirae.upgrade.imprint.ImprintRemoveRecipe;
import com.themrsung.mirae.upgrade.tool.MagnetRecipe;
import com.themrsung.mirae.upgrade.weapon.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Upgrades for items.
 */
public final class Upgrades {
    private static final @NotNull Set<UpgradeRecipe> RECIPES = Set.of(
            new TestUpgradeRecipe(),

            new StormbreakerRecipe(),
            new ThorHammerRecipe(),
            new CaptainShieldRecipe(),

            new ClosedLightsaberRecipe(),
            new ColoredLightsaberRecipe(),

            new BaseballBatRecipe(),

            new ImprintRemoveRecipe(),

            new NetheriteToVibraniumRecipe(),
            new CoalToDiamondRecipe(),
            new IronToolToDiamondRecipe(),
            new WoodenToolToNetheriteRecipe(),

            new MagnetRecipe(),

            new CustomEnchantApplicationRecipe(),
            new CustomEnchantAcquisitionRecipe()
    );

    /**
     * Returns the set of upgrade recipes.
     *
     * @return The set of upgrade recipes
     */
    public static @NotNull Set<UpgradeRecipe> getRecipes() {
        return RECIPES;
    }

    /**
     * Returns the probability of success for the given skill level.
     *
     * @param defaultRate The base rate
     * @param skillLevel  The skill level
     * @return The modified success rate
     */
    public static double getSuccessRateForSkillLevel(double defaultRate, long skillLevel) {
        if (defaultRate >= 1) return defaultRate; // No need to modify

        // Add 0.4%p per level
        double probabilityPoints = (double) Math.min(skillLevel, 100) / 250;
        return Math.min(defaultRate + probabilityPoints, 0.99);
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Upgrades() throws Exception {
        throw new Exception("Cannot instantiate class.");
    }
}
