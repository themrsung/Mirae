package com.themrsung.mirae.account;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An account title.
 */
public enum AccountTitle {
    EMPTY("empty", Component.empty()),

    NETHER_STAR("nether_star", Component.text(":mc_nether_star:")),

    CLOCK("clock", Component.text(":mc_clock:")),

    RUBY("ruby", Component.text(":mc_ruby:")),

    ELYTRA("elytra", Component.text(":mc_elytra:")),

    BARRIER("barrier", Component.text(":mc_barrier:"));

    AccountTitle(@NotNull String key, @NotNull Component value) {
        this.key = key;
        this.value = value;
    }

    /// Titles

    public static @NotNull AccountTitle getOrEmpty(@Nullable String key) {
        if (key == null) return EMPTY;

        try {
            return valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EMPTY;
        }
    }

    public static @NotNull Set<String> getKeys() {
        return Arrays.stream(values())
                .map(AccountTitle::toString)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());
    }

    /// Body

    private final @NotNull String key;
    private final @NotNull Component value;

    /**
     * Returns the key.
     *
     * @return The key
     */
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    public @NotNull Component getValue() {
        return value;
    }
}
