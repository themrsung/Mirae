package com.themrsung.mirae.economy;

import com.themrsung.mirae.command.economy.CoinCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Coin version.
 */
public enum CoinVersion {
    /**
     * Created 2025-08-01. Uses ItemsAdder coin item.
     */
    VERSION_1(1, Material.STICK),

    ;

    /**
     * The current version.
     */
    public static final @NotNull CoinVersion CURRENT = VERSION_1;

    /**
     * The set of supported versions.
     */
    private static final @NotNull EnumSet<CoinVersion> SUPPORTED_VERSIONS = EnumSet.of(VERSION_1);

    /**
     * Returns the set of supported versions.
     *
     * @return The set of supported versions
     */
    public static @NotNull EnumSet<CoinVersion> getSupportedVersions() {
        return EnumSet.copyOf(SUPPORTED_VERSIONS);
    }

    /**
     * Returns whether the version is supported.
     *
     * @param version The version
     * @return {@code true} if supported
     */
    public static boolean isSupportedVersion(@Nullable CoinVersion version) {
        return SUPPORTED_VERSIONS.contains(version);
    }

    /**
     * Returns whether the item is a valid coin.
     *
     * @param item The item to check
     * @return {@code true} if valid coin
     */
    public static boolean isValidCoin(@Nullable ItemStack item) {
        return CoinVersion.getSupportedVersions().stream().anyMatch(v -> v.isCoin(item));
    }

    /**
     * Creates a new coin version.
     *
     * @param ordinal  The constant ordinal
     * @param itemType The item type
     */
    CoinVersion(int ordinal, @NotNull Material itemType) {
        this.ordinal = ordinal;
        this.itemType = itemType;

    }

    private final int ordinal;
    private final @NotNull Material itemType;

    /**
     * Returns the constant ordinal.
     *
     * @return The constant ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Returns the item type.
     *
     * @return The item type
     */
    public @NotNull Material getItemType() {
        return itemType;
    }

    /**
     * Returns whether the item is a coin of this version.
     *
     * @param coin The coin
     * @return {@code true} if it is a coin
     */
    public boolean isCoin(@Nullable ItemStack coin) {
        return switch (this) {
            default -> CoinCommand.getCoinItem(this).isSimilar(coin);
        };
    }
}
