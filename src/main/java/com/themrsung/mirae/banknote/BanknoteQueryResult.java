package com.themrsung.mirae.banknote;

import com.themrsung.mirae.item.economy.Banknote;
import org.jetbrains.annotations.Nullable;

/**
 * Banknote validity query result.
 *
 * @param result  The result
 * @param version The version
 * @param amount  The amount
 */
public record BanknoteQueryResult(
        boolean result,
        @Nullable Banknote.Version version,
        double amount
) {
    /**
     * Creates a false result.
     *
     * @param result {@code false}
     */
    public BanknoteQueryResult(boolean result) {
        this(result, null, Double.NaN);
    }

    /**
     * Returns whether this result is valid.
     *
     * @return {@code true} if valid
     */
    public boolean valid() {
        return version != null && version.isValid();
    }
}
