package com.themrsung.mirae.item;

import org.jetbrains.annotations.NotNull;

/**
 * A modifiable custom item.
 */
public interface Modifiable extends CustomItem {
    /**
     * Returns the unique namespace key.
     *
     * @return The unique namespace key
     */
    @NotNull String getUniqueKey();
}
