package com.themrsung.mirae.account;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * An account title.
 */
public final class AccountTitle implements Serializable {
    /**
     * Empty title.
     */
    public static @NotNull AccountTitle EMPTY = new AccountTitle("empty", Component.empty());

    /**
     * Creates a new title.
     *
     * @param key   The key
     * @param value The value
     * @return The title
     */
    public static @NotNull AccountTitle createTitle(@NotNull String key, @NotNull Component value) {
        return new AccountTitle(key, value);
    }

    ///
    /// Body
    ///

    /**
     * Creates a new title.
     *
     * @param key   The key
     * @param value The value
     */
    private AccountTitle(@NotNull String key, @NotNull Component value) {
        this.key = key;
        this.value = value;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AccountTitle t)) return false;
        return Objects.equals(key, t.key) && Objects.equals(value, t.value);
    }
}
