package com.themrsung.mirae.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * A carousel list.
 *
 * @param <T> The type
 */
public class Carousel<T> extends ArrayList<T> {
    /**
     * Returns a new carousel of the given elements.
     *
     * @param args The arguments
     * @param <U>  The type
     * @return The carousel
     */
    @SafeVarargs
    public static <U> @NotNull Carousel<U> of(@NotNull U... args) {
        return new Carousel<>(new Vector<>(Arrays.asList(args)));
    }

    /**
     * Creates a new carousel.
     */
    public Carousel() {
        super();

        this.increment = MutableIncrement.zero();
    }

    /**
     * Creates a new carousel.
     *
     * @param c The collection to copy
     */
    public Carousel(@NotNull Collection<? extends T> c) {
        super(c);

        if (c instanceof Carousel<? extends T> carousel) {
            this.increment = MutableIncrement.copyOf(carousel.increment);
        } else {
            this.increment = MutableIncrement.zero();
        }
    }

    protected final @NotNull MutableIncrement increment;

    /**
     * Returns the increment.
     *
     * @return The increment
     */
    public @NotNull MutableIncrement getIncrement() {
        return increment;
    }

    /**
     * Returns the next element.
     *
     * @return The next element
     */
    public T getNext() {
        if (increment.get() >= size()) {
            increment.set(size() - 1);
        }

        return get(increment.incrementMod(size()));
    }
}
