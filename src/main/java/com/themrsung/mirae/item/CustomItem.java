package com.themrsung.mirae.item;

import com.themrsung.mirae.item.economy.DonorCoin;
import com.themrsung.mirae.item.food.*;
import com.themrsung.mirae.item.weapon.*;
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

    /**
     * Closed lightsaber.
     */
    @NotNull CustomItem CLOSED_LIGHTSABER = new ClosedLightsaber();

    /**
     * Baseball bat.
     */
    @NotNull CustomItem BASEBALL_BAT = new BaseballBat();

    /**
     * Magnet.
     */
    @NotNull CustomItem MAGNET = new ModifiableItemsAdderItem("magnet:magnet", "mirae.item.magnet");

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
     * Salad.
     */
    @NotNull CustomItem SALAD = new Salad();

    /**
     * Salt.
     */
    @NotNull CustomItem SALT = new ModifiableItemsAdderItem("food:salt", "mirae.food.salt");

    /**
     * Salt bread.
     */
    @NotNull CustomItem SALT_BREAD = new SaltBread();

    /**
     * Sausage.
     */
    @NotNull CustomItem SAUSAGE = new Sausage();

    /// DRUGS

    /**
     * Coca paste.
     */
    @NotNull CustomItem COCA_PASTE = new ItemsAdderItem("drugs:coca_paste");

    /**
     * Cocaine.
     */
    @NotNull CustomItem COCAINE = new ItemsAdderItem("drugs:cocaine");

    /**
     * Crack Cocaine.
     */
    @NotNull CustomItem CRACK_COCAINE = new ItemsAdderItem("drugs:crack_cocaine");

    /**
     * Heroin.
     */
    @NotNull CustomItem HEROIN = new ItemsAdderItem("drugs:heroin");

    /**
     * Morphine.
     */
    @NotNull CustomItem MORPHINE = new ItemsAdderItem("drugs:morphine");

    /**
     * Opium.
     */
    @NotNull CustomItem OPIUM = new ItemsAdderItem("drugs:opium");

    /**
     * Checks if the given item instance is this custom item.
     *
     * @param item The item to check
     * @return {@code true} if items match
     */
    boolean isItem(@Nullable ItemStack item);
}
