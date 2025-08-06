package com.themrsung.mirae.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A pair of objects.
 *
 * @param first  The first object
 * @param second The second object
 * @param <T>    The type
 */
public record Pair<T>(T first, T second) implements Iterable<T> {
    /**
     * Returns the reversed pair.
     *
     * @return The reversed pair
     */
    public @NotNull Pair<T> reversed() {
        return new Pair<>(second, first);
    }

    /**
     * Returns whether this pair is equal to the given pair, regardless of order.
     *
     * @param p The pair
     * @return {@code true} if equals
     */
    public boolean equalsIgnoreOrder(@NotNull Pair<? super T> p) {
        return equals(p) || (Objects.equals(first, p.second) && Objects.equals(second, p.first));
    }

    /**
     * Returns a stream.
     *
     * @return The stream
     */
    public @NotNull Stream<T> stream() {
        return Stream.of(first, second);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?> p)) return false;
        return Objects.equals(first, p.first) && Objects.equals(second, p.second);
    }
}
