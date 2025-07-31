package com.themrsung.mirae.util;

import org.jetbrains.annotations.NotNull;

/**
 * Mutable {@code i}
 */
public final class MutableIncrement {
    /**
     * Creates and returns a new mutable increment.
     *
     * @return The new increment
     */
    public static @NotNull MutableIncrement zero() {
        return new MutableIncrement(0);
    }

    /**
     * Creates and returns a new mutable increment.
     *
     * @param i The starting value
     * @return The new increment
     */
    public static @NotNull MutableIncrement at(int i) {
        return new MutableIncrement(i);
    }

    /**
     * Creates a new mutable increment.
     *
     * @param i The starting index
     */
    private MutableIncrement(int i) {
        this.i = 0;
    }

    private int i;

    /**
     * Returns the value.
     *
     * @return {@code i}
     */
    public int get() {
        return i;
    }

    /**
     * Increments and returns.
     *
     * @return {@code i}
     */
    public int increment() {
        return i++;
    }

    /**
     * Decrements and returns.
     *
     * @return {@code i}
     */
    public int decrement() {
        return i--;
    }
}
