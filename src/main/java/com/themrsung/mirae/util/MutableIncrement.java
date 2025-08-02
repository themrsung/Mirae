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
     * Returns a copy of the given increment.
     *
     * @param inc The increment
     * @return The copy
     */
    public static @NotNull MutableIncrement copyOf(@NotNull MutableIncrement inc) {
        return new MutableIncrement(inc.i);
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
     * Sets the value.
     *
     * @param i The value
     */
    public void set(int i) {
        this.i = i;
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
     * Increments mod and returns.
     *
     * @param mod The mod
     * @return {@code i}
     */
    public int incrementMod(int mod) {
        int before = i;
        i = (i + 1) % mod;
        return before;
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
