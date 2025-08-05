package com.themrsung.mirae.item;

import com.themrsung.mirae.item.avengers.CaptainShield;
import com.themrsung.mirae.item.avengers.Stormbreaker;
import com.themrsung.mirae.item.avengers.ThorHammer;
import com.themrsung.mirae.item.economy.DonorCoin;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom item.
 */
public interface CustomItem {
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

    /// MATERIALS

    /**
     * Vibranium ore.
     */
    @NotNull CustomItem VIBRANIUM_ORE = new ItemsAdderItem("ultimate_armors:dark_ore");

    /**
     * Raw Vibranium.
     */
    @NotNull CustomItem VIBRANIUM_RAW = new ItemsAdderItem("ultimate_armors:raw_dark");

    /**
     * Vibranium ingot.
     */
    @NotNull CustomItem VIBRANIUM_INGOT = new ItemsAdderItem("ultimate_armors:dark_ingot");

    /**
     * Uranium ore.
     */
    @NotNull CustomItem URANIUM_ORE = new ItemsAdderItem("ultimate_armors:jade_ore");

    /**
     * Raw Uranium.
     */
    @NotNull CustomItem URANIUM_RAW = new ItemsAdderItem("ultimate_armors:raw_jade");

    /**
     * Uranium ingot.
     */
    @NotNull CustomItem URANIUM_INGOT = new ItemsAdderItem("ultimate_armors:jade_ingot");

    /**
     * Returns a new item instance.
     *
     * @return The item
     */
    @NotNull ItemStack getItem();

    /**
     * Checks if the given item instance is this custom item.
     *
     * @param item The item to check
     * @return {@code true} if items match
     */
    boolean isItem(@Nullable ItemStack item);
}
