package com.themrsung.mirae;

import com.themrsung.mirae.account.Account;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * MX main utility class.
 */
public final class MX {
    ///
    /// Styles
    ///

    public static final Style STYLE_NORMAL = Style.style()
            .color(TextColor.fromHexString("#efedee"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_SPECIAL = Style.style()
            .color(TextColor.fromHexString("#72f7ff"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_GOOD = Style.style()
            .color(TextColor.fromHexString("#23df3a"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_WARNING = Style.style()
            .color(TextColor.fromHexString("#fdf428"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_ERROR = Style.style()
            .color(TextColor.fromHexString("#df3224"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_BUY = Style.style()
            .color(TextColor.fromHexString("#df4b3d"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    public static final Style STYLE_SELL = Style.style()
            .color(TextColor.fromHexString("#332eff"))
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .build();

    ///
    /// Accounts
    ///

    /**
     * Requires non-null for account.
     *
     * @param account The account
     * @return The account
     */
    public static @NotNull Account requireAccountNonNull(@Nullable Account account) {
        return requireAccountNonNull(account, null);
    }

    /**
     * Requires non-null for account.
     *
     * @param account The account
     * @param message The message to log if null
     * @return The account
     */
    public static @NotNull Account requireAccountNonNull(@Nullable Account account, @Nullable String message) {
        if (account == null) {
            Mirae.getInstance().getLogger().severe(Objects.requireNonNullElse(message, "Account is null where it shouldn't be."));
            throw new RuntimeException(Objects.requireNonNullElse(message, "Account is null where it shouldn't be."));
        }

        return account;
    }

    ///
    /// Components
    ///

    /**
     * Checks if the given component is blank.
     *
     * @param component The component to check
     * @return {@code true} if the component is blank
     */
    public static boolean isBlank(@Nullable Component component) {
        if (component == null) return true;
        return Objects.equals(Component.empty(), component);
    }

    public static @NotNull Component stylizeText(@Nullable String text) {
        if (text == null) return Component.empty();
        return Component.text(text).style(Style.style().color(TextColor.fromHexString("#ffffff")).build());
    }

    ///
    /// Inventory
    ///

    /**
     * Counts the number of items in the given inventory.
     *
     * @param inventory The inventory to check
     * @param item      The item to check for
     * @return The number of items
     */
    public static int countItems(@NotNull Inventory inventory, @NotNull ItemStack item) {
        ItemStack[] contents = inventory.getContents();

        int count = 0;

        for (var content : contents) {
            if (content != null && item.isSimilar(content)) {
                count += content.getAmount();
            }
        }

        return count;
    }

    /**
     * Counts the number of items in the given inventory.
     *
     * @param inventory The inventory to check
     * @param type      The type of item to check
     * @return The number of items
     */
    public static int countItems(@NotNull Inventory inventory, @NotNull Material type) {
        ItemStack[] contents = inventory.getContents();

        int count = 0;

        for (var content : contents) {
            if (content != null && content.getType() == type) {
                count += content.getAmount();
            }
        }

        return count;
    }

    /**
     * Returns the remaining space for the given item within the given inventory.
     *
     * @param inventory The inventory
     * @param item      The item
     * @return The available space
     */
    public static int getRemainingSpaceFor(@NotNull Inventory inventory, @NotNull ItemStack item) {
        int queryLimit = (inventory instanceof PlayerInventory) ? 36 : -1;
        int stackSize = item.getType().getMaxStackSize();
        int space = stackSize * Math.max(inventory.getSize(), queryLimit);

        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < Math.max(contents.length, queryLimit); i++) {
            ItemStack stack = contents[i];

            if (!item.isSimilar(stack)) {
                space -= stackSize;
            } else {
                int occupied = stack.getAmount();
                space -= occupied;
            }
        }

        return space;
    }

    /**
     * Gives items to the given inventory.
     *
     * @param inventory The inventory to give to
     * @param items     The item stack to give
     * @return The remaining amount which was unable to fit
     */
    public static int giveItems(@NotNull Inventory inventory, @NotNull ItemStack items) {
        int availableSpace = getRemainingSpaceFor(inventory, items);

        inventory.addItem(items);

        return items.getAmount() - availableSpace;
    }

    /**
     * Takes items from the given inventory.
     *
     * @param inventory The inventory to take from
     * @param items     The item stack to take
     * @return The remaining amount which was unable to be taken
     */
    public static int takeItems(@NotNull Inventory inventory, @NotNull ItemStack items) {
        int remaining = items.getAmount();
        int stackSize = items.getType().getMaxStackSize();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || !items.isSimilar(item)) continue;

            int toRemove = Math.min(Math.min(stackSize, item.getAmount()), remaining);

            if (item.getAmount() == toRemove) {
                inventory.clear(i);
                remaining -= toRemove;
            } else {
                int previous = item.getAmount();
                item.setAmount(previous - toRemove);
                remaining -= toRemove;
            }
        }

        return remaining;
    }

    ///
    /// GSON
    ///


    ///
    /// Misc.
    ///

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private MX() throws Exception {
        throw new Exception("Cannot instantiate utility class MX.");
    }
}
