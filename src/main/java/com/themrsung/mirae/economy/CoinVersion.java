package com.themrsung.mirae.economy;

import com.themrsung.mirae.command.economy.CoinCommand;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public enum CoinVersion {
    VERSION_1(1, Material.STICK),

    ;

    public static final @NotNull CoinVersion CURRENT = VERSION_1;

    private static final @NotNull EnumSet<CoinVersion> SUPPORTED_VERSIONS = EnumSet.of(VERSION_1);

    public static @NotNull EnumSet<CoinVersion> getSupportedVersions() {
        return EnumSet.copyOf(SUPPORTED_VERSIONS);
    }

    public static boolean isSupportedVersion(@Nullable CoinVersion version) {
        return SUPPORTED_VERSIONS.contains(version);
    }

    public static boolean isValidCoin(@Nullable ItemStack item) {
        return CoinVersion.getSupportedVersions().stream().anyMatch(v -> v.isCoin(item));
    }

    CoinVersion(int ordinal, @NotNull Material item) {
        this.ordinal = ordinal;
        this.item = item;

    }

    private final int ordinal;
    private final @NotNull Material item;

    public int getOrdinal() {
        return ordinal;
    }

    public @NotNull Material getItem() {
        return item;
    }

    private boolean isCoin(@Nullable ItemStack coin) {
        return switch (this) {
            default -> CoinCommand.getCoinItem(this).isSimilar(coin);
        };
    }
}
