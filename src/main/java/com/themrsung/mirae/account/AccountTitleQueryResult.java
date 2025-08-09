package com.themrsung.mirae.account;

import org.jetbrains.annotations.Nullable;

/**
 * Account title item validity query result.
 *
 * @param result The result
 * @param title  The title
 */
public record AccountTitleQueryResult(
        boolean result,
        @Nullable AccountTitle title
) {
}
