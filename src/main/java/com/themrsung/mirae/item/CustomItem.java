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
     * Mythic hammer.
     */
    @NotNull CustomItem MYTHIC_HAMMER = new MythicHammer();

    /**
     * StormHammer.
     */
    @NotNull CustomItem STORM_HAMMER = new StormHammer();

    /**
     * Captain America's shield.
     */
    @NotNull CustomItem HERO_SHIELD = new HeroShield();

    /**
     * Metal claws.
     */
    @NotNull CustomItem METAL_CLAWS = new MetalClaws();

    /**
     * Blue beam sword.
     */
    @NotNull CustomItem BLUE_BEAM_SWORD = new BlueBeamSword();

    /**
     * Red beam sword.
     */
    @NotNull CustomItem RED_BEAM_SWORD = new RedBeamSword();

    /**
     * Green beam sword.
     */
    @NotNull CustomItem GREEN_BEAM_SWORD = new GreenBeamSword();

    /**
     * Closed beam sword.
     */
    @NotNull CustomItem CLOSED_BEAM_SWORD = new ClosedBeamSword();

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
     * Raw darksteel.
     */
    @NotNull CustomItem DARKSTEEL_RAW = new ItemsAdderItem("ultimate_armors:raw_dark");

    /**
     * Darksteel ingot.
     */
    @NotNull CustomItem DARKSTEEL_INGOT = new ItemsAdderItem("ultimate_armors:dark_ingot");

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
     * Advanced steak.
     */
    @NotNull CustomItem ADVANCED_STEAK = new AdvancedSteak();

    /**
     * Apple pie.
     */
    @NotNull CustomItem APPLE_PIE = new ApplePie();

    /**
     * Army stew.
     */
    @NotNull CustomItem ARMY_STEW = new ArmyStew();

    /**
     * Gamja tang.
     */
    @NotNull CustomItem GAMJA_TANG = new GamjaTang();

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
     * Scrambled egg.
     */
    @NotNull CustomItem SCRAMBLED_EGG = new ScrambledEgg();

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
