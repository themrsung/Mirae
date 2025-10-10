package com.themrsung.mirae;

import com.themrsung.mirae.account.Account;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * MX main utility class.
 */
public final class MX {
    /// Styles

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
    /// GriefPrevention
    ///

    /**
     * Returns the GriefPrevention instance.
     *
     * @return The instance
     */
    public static @NotNull GriefPrevention getGriefPrevention() {
        return GriefPrevention.instance;
    }

    ///
    /// Accounts
    ///

    /**
     * Returns a stream of all online accounts.
     * @return A stream of online accounts
     */
    public static @NotNull Stream<Account> getOnlineAccounts() {
        return Mirae.getState().getAccounts().stream()
                .filter(account -> account.getOfflinePlayer().isOnline());
    }

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
     * @param message The content to log if null
     * @return The account
     */
    public static @NotNull Account requireAccountNonNull(@Nullable Account account, @Nullable String message) {
        if (account == null) {
            Mirae.getInstance().getLogger().severe(Objects.requireNonNullElse(message, "Account is null where it shouldn't be."));
            throw new RuntimeException(Objects.requireNonNullElse(message, "Account is null where it shouldn't be."));
        }

        return account;
    }

    /// Location

    public static @NotNull String locationToReadableString(@Nullable Location location) {
        if (location == null) return "없음";

        return "[" + location.getWorld().getName() + ", "
                + Math.round(location.getX()) + ", "
                + Math.round(location.getY()) + ", "
                + Math.round(location.getZ()) + "]";
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
     * Returns a resized stack.
     *
     * @param stack    The stack
     * @param quantity The quantity
     * @return The resized stack
     */
    @Contract(pure = true)
    public static @NotNull ItemStack resizedStack(@NotNull ItemStack stack, int quantity) {
        ItemStack cloned = stack.clone();
        cloned.setAmount(quantity);
        return cloned;
    }

    /**
     * Returns a resized stack.
     *
     * @param stack    The stack
     * @param function The quantity modifier function
     * @return The resized stack
     */
    @Contract(pure = true)
    public static @NotNull ItemStack resizedStack(@NotNull ItemStack stack, @NotNull IntUnaryOperator function) {
        ItemStack cloned = stack.clone();
        cloned.setAmount(function.applyAsInt(stack.getAmount()));
        return cloned;
    }

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
        int queryLimit = (inventory instanceof PlayerInventory) ? 36 : inventory.getSize();
        int stackSize = item.getType().getMaxStackSize();
        int space = stackSize * queryLimit;

        ItemStack[] contents = inventory.getStorageContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];

            if (stack == null || stack.getType() == Material.AIR) continue;

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
     * Gives items to the given inventory. Use {@link #giveItems(Player, ItemStack)} to give to players.
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
     * Gives items to the given player.
     *
     * @param player The player to give to
     * @param items  The item(s) to give
     */
    public static void giveItems(@NotNull Player player, @NotNull ItemStack items) {
        giveItems(player, items, false);
    }

    /**
     * Gives items to the given player.
     *
     * @param player                 The player to give to
     * @param items                  The item(s) to give
     * @param mustDeliverToInventory {@code true} if items MUST be delivered to the inventory
     * @return The remaining number of items which could not fit
     */
    public static int giveItems(@NotNull Player player, @NotNull ItemStack items, boolean mustDeliverToInventory) {
        PlayerInventory inventory = player.getInventory();
        int remainder = giveItems(inventory, items);

        if (mustDeliverToInventory || remainder <= 0) {
            return remainder;
        }

        ItemStack remaining = resizedStack(items, remainder);

        player.getWorld().dropItem(player.getLocation(), remaining);
        return 0;
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
    /// Legacy
    ///

    /**
     * Formats standard balance.
     *
     * @param balance The balance
     * @return The formatted balance
     */
    public static @NotNull String formatBalance(double balance) {
        return NumberFormat.getNumberInstance().format(balance) + "원";
    }

    /**
     * Formats premium balance.
     *
     * @param balance The premium balance
     * @return The formatted balance
     */
    public static @NotNull String formatCoinBalance(long balance) {
        return NumberFormat.getNumberInstance().format(balance) + "코인";
    }

    /**
     * Tries to execute and return the value of the getter function.
     *
     * @param getter   The getter function
     * @param fallback The fallback value
     * @param <T>      The parameter type
     * @return The getter function's return value if successful, {@code fallback} otherwise
     */
    public static <T> T tryOrElse(@NotNull Supplier<T> getter, T fallback) {
        try {
            return getter.get();
        } catch (Throwable e) {
            return fallback;
        }
    }

    /**
     * Tries to execute the getter function and returns the value.
     *
     * @param getter   The getter function
     * @param fallback The fallback getter function
     * @param <T>      The parameter type
     * @return The result of the getter function if successful, the result of the fallback function otherwise
     * @throws RuntimeException When an exception occurs during the execution of the fallback getter
     */
    public static <T> T tryOrElseGet(Supplier<? extends T> getter, Supplier<? extends T> fallback) throws RuntimeException {
        try {
            return getter.get();
        } catch (Throwable ignored) {
            try {
                return fallback.get();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static final Map<String, Long> KOREAN_UNITS;

    static {
        var units = new HashMap<String, Long>();

        units.put("조", 10000 * 10000 * 10000L);
        units.put("천억", 10000 * 10000L * 1000);
        units.put("백억", 10000 * 10000L * 100);
        units.put("십억", 10000 * 10000L * 10);
        units.put("억", 10000 * 10000L);
        units.put("천만", 10000L * 1000);
        units.put("백만", 10000L * 100);
        units.put("십만", 10000L * 10);
        units.put("만", 10000L);
        units.put("천", 1000L);
        units.put("백", 100L);
        units.put("십", 10L);

        KOREAN_UNITS = Map.copyOf(units);
    }

    public static final List<String> NUMBER_EXAMPLES = List.of(
            "10000",
            "5만",
            "십만"
    );

    /**
     * Parses the given string into a {@code double} value.
     *
     * @param input The input to parse
     * @return The parsed double
     */
    public static double parseDouble(String input) {
        return tryOrElse(() -> tryOrElseGet(
                () -> Double.parseDouble(input
                        .replace(",", "")
                        .replace("원", "")
                        .replace("코인", "")
                        .replace("_", "")
                        .replace(" ", "")
                        .trim()),
                () -> {
                    var cleaned = input
                            .replace(",", "")
                            .replace("원", "")
                            .replace("코인", "")
                            .replace("_", "")
                            .replace(" ", "")
                            .trim();

                    if (KOREAN_UNITS.containsKey(cleaned)) {
                        return (double) KOREAN_UNITS.get(cleaned);
                    }

                    for (var entry : KOREAN_UNITS.entrySet()) {
                        var unit = entry.getKey();
                        var multiplier = entry.getValue();

                        if (cleaned.endsWith(unit)) {
                            var numberPart = cleaned.substring(0, cleaned.length() - unit.length()).trim();
                            if (numberPart.isEmpty()) return (double) multiplier;

                            var number = Double.parseDouble(numberPart);
                            return number * multiplier;
                        }
                    }

                    throw new NumberFormatException("Unable to parse number.");
                }
        ), 0d);
    }

    public static @NotNull String getKoreanMaterialName(@NotNull Material material) {
        return switch (material) {
            case DRAGON_EGG -> "Nine_heads의 알";
            case DRAGON_HEAD -> "Nine_heads의 첫번째 머리";
            case DRAGON_BREATH -> "Nine_heads의 숨결";
            case NETHER_STAR -> "asqwzx의 별";

            case NETHERITE_SWORD -> "네더라이트 칼";
            case NETHERITE_AXE -> "네더라이트 도끼";
            case NETHERITE_PICKAXE -> "네더라이트 드릴";
            case NETHERITE_SHOVEL -> "네더라이트 숟가락";
            case NETHERITE_HOE -> "네더라이트 크로우바";

            case DIAMOND_SWORD -> "다이아 용검";
            case DIAMOND_AXE -> "다이아 곡괭이 아닌 도끼";
            case DIAMOND_PICKAXE -> "다이아 곡괭이";
            case DIAMOND_SHOVEL -> "다이아 삽질기";
            case DIAMOND_HOE -> "다이아 밭쟁이";

            case GOLDEN_SWORD -> "고급 칼";
            case GOLDEN_AXE -> "고급 도끼";
            case GOLDEN_PICKAXE -> "고급 곡괭이";
            case GOLDEN_SHOVEL -> "고급 삽";
            case GOLDEN_HOE -> "고급 괭이";

            case IRON_SWORD -> "철 검";
            case IRON_AXE -> "전투 도끼";
            case IRON_PICKAXE -> "철 곡괭이";
            case IRON_SHOVEL -> "야전삽";
            case IRON_HOE -> "철 괭이";

            case STONE_SWORD -> "돌 칼";
            case STONE_AXE -> "돌 도끼";
            case STONE_PICKAXE -> "돌 곡괭이";
            case STONE_SHOVEL -> "돌 삽";
            case STONE_HOE -> "돌 괭이";

            case WOODEN_SWORD -> "나무 칼";
            case WOODEN_AXE -> "나무 도끼";
            case WOODEN_PICKAXE -> "나무 곡괭이";
            case WOODEN_SHOVEL -> "나무 삽";
            case WOODEN_HOE -> "나무 괭이";

            case NETHERITE_HELMET -> "네더라이트 ";
            case NETHERITE_CHESTPLATE -> "네더라이트 방탄복";
            case NETHERITE_LEGGINGS -> "네더라이트 전투 바지";
            case NETHERITE_BOOTS -> "네더라이트 전투화";

            case DIAMOND_HELMET -> "다이아몬드 안전모";
            case DIAMOND_CHESTPLATE -> "다이아 작업복 상의";
            case DIAMOND_LEGGINGS -> "다이아 작업복 하의";
            case DIAMOND_BOOTS -> "다이아 안전화";

            case GOLDEN_HELMET -> "황금 머리띠";
            case GOLDEN_CHESTPLATE -> "황금 조끼";
            case GOLDEN_LEGGINGS -> "황금 레깅스";
            case GOLDEN_BOOTS -> "황금 구두";

            case IRON_HELMET -> "철 투구";
            case IRON_CHESTPLATE -> "철 갑옷";
            case IRON_LEGGINGS -> "철 바지";
            case IRON_BOOTS -> "철 신발";

            case CHAINMAIL_HELMET -> "고급 모자";
            case CHAINMAIL_CHESTPLATE -> "고급 자켓";
            case CHAINMAIL_LEGGINGS -> "고급 바지";
            case CHAINMAIL_BOOTS -> "고급 신발";

            case LEATHER_HELMET -> "이상한 모자";
            case LEATHER_CHESTPLATE -> "이상한 상의";
            case LEATHER_LEGGINGS -> "이상한 하의";
            case LEATHER_BOOTS -> "이상한 신발";

            case SHIELD -> "시위 방패";
            case MACE -> "망치";

            case COOKED_BEEF -> "스테이크";

            case COPPER_INGOT -> "금괴 (스페인산)";
            case COPPER_BLOCK -> "금 블럭 (스페인산)";

            case BARRIER -> "어?";
            case BEDROCK -> "어??";

            default -> material.toString().replace("_", " ");
        };
    }

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
