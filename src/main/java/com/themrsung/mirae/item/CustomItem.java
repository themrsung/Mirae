package com.themrsung.mirae.item;

import com.themrsung.mirae.item.avengers.CaptainShield;
import com.themrsung.mirae.item.avengers.MetalClaws;
import com.themrsung.mirae.item.avengers.Stormbreaker;
import com.themrsung.mirae.item.avengers.ThorHammer;
import com.themrsung.mirae.item.economy.DonorCoin;
import com.themrsung.mirae.item.food.*;
import com.themrsung.mirae.item.starwars.BlueLightsaber;
import com.themrsung.mirae.item.starwars.GreenLightsaber;
import com.themrsung.mirae.item.starwars.RedLightsaber;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom item.
 */
public interface CustomItem extends ItemSupplier {
    /// TICKET / COUPONS

    /**
     * Upgrade success ticket. Guarantees successful upgrade.
     */
    @NotNull CustomItem UPGRADE_SUCCESS_TICKET = new ItemsAdderItem("iageneric:upgrade_success_ticket");

    /**
     * Donor coin.
     */
    @NotNull CustomItem DONOR_COIN = new DonorCoin();

    /// TOOLS

    /**
     * Thor's Hammer.
     */
    @NotNull CustomItem THOR_HAMMER = new ThorHammer();

    /**
     * Stormbreaker.
     */
    @NotNull CustomItem STORMBREAKER = new Stormbreaker();

    /**
     * Captain America's shield.
     */
    @NotNull CustomItem CAPTAIN_SHIELD = new CaptainShield();

    /**
     * Metal claws.
     */
    @NotNull CustomItem METAL_CLAWS = new MetalClaws();

    /**
     * Blue lightsaber.
     */
    @NotNull CustomItem BLUE_LIGHTSABER = new BlueLightsaber();

    /**
     * Red lightsaber.
     */
    @NotNull CustomItem RED_LIGHTSABER = new RedLightsaber();

    /**
     * Green lightsaber.
     */
    @NotNull CustomItem GREEN_LIGHTSABER = new GreenLightsaber();

    /// MATERIALS

    /**
     * Raw Vibranium.
     */
    @NotNull CustomItem VIBRANIUM_RAW = new ItemsAdderItem("ultimate_armors:raw_dark");

    /**
     * Vibranium ingot.
     */
    @NotNull CustomItem VIBRANIUM_INGOT = new ItemsAdderItem("ultimate_armors:dark_ingot");

    /**
     * Raw Uranium.
     */
    @NotNull CustomItem URANIUM_RAW = new ItemsAdderItem("ultimate_armors:raw_jade");

    /**
     * Uranium ingot.
     */
    @NotNull CustomItem URANIUM_INGOT = new ItemsAdderItem("ultimate_armors:jade_ingot");

    /// FOODS

    /**
     * Apple pie.
     */
    @NotNull CustomItem APPLE_PIE = new ApplePie();

    /**
     * Army stew.
     */
    @NotNull CustomItem ARMY_STEW = new ArmyStew();

    /**
     * Ham.
     */
    @NotNull CustomItem HAM = new Ham();

    /**
     * Salad
     */
    @NotNull CustomItem SALAD = new Salad();

    /**
     * Salt bread.
     */
    @NotNull CustomItem SALT_BREAD = new SaltBread();

    /**
     * Sausage.
     */
    @NotNull CustomItem SAUSAGE = new Sausage();

    /**
     * Checks if the given item instance is this custom item.
     *
     * @param item The item to check
     * @return {@code true} if items match
     */
    boolean isItem(@Nullable ItemStack item);
}
