package com.themrsung.mirae.item.storage;

import com.google.gson.*;
import com.themrsung.mirae.gson.ItemStackLongPair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Infinite item storage backed by a map of {@link ItemStack} keys and {@link Long} quantities.
 */
public final class ItemStorage implements Serializable {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    public static @NotNull JsonSerializer<ItemStorage> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    public static @NotNull JsonDeserializer<ItemStorage> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Standardizes the provided item stack before hashing.
     *
     * @param item The item stack to standardize
     * @return A new standardized clone of the provided item stack
     */
    public static @NotNull ItemStack standardize(@NotNull ItemStack item) {
        Objects.requireNonNull(item, "item");

        ItemStack clone = item.clone();
        clone.setAmount(1);
        return clone;
    }

    /**
     * Creates a new item storage instance.
     */
    public ItemStorage() {
        this.items = new ConcurrentHashMap<>();
    }

    private final @NotNull Map<ItemStack, Long> items;

    /**
     * Adds the specified amount of the provided item stack into storage.
     *
     * @param item   The item stack to add
     * @param amount The amount to add (must be positive)
     */
    public synchronized void addItem(@NotNull ItemStack item, long amount) {
        Objects.requireNonNull(item, "item");
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Cannot store air.");
        }

        ItemStack key = standardize(item);
        items.merge(key, amount, Long::sum);
    }

    /**
     * Removes items from storage.
     *
     * @param item   The item stack to remove
     * @param amount The desired amount to remove (must be positive)
     * @return The amount actually removed (0 if nothing removed)
     */
    public synchronized long removeItem(@NotNull ItemStack item, long amount) {
        Objects.requireNonNull(item, "item");
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        ItemStack key = standardize(item);
        Long current = items.get(key);
        if (current == null || current <= 0) {
            return 0;
        }

        long toRemove = Math.min(amount, current);
        long remaining = current - toRemove;
        if (remaining <= 0) {
            items.remove(key);
        } else {
            items.put(key, remaining);
        }
        return toRemove;
    }

    /**
     * Returns the stored amount for the provided item stack.
     *
     * @param item The item stack
     * @return The amount stored (0 if not present)
     */
    public synchronized long getAmount(@NotNull ItemStack item) {
        Objects.requireNonNull(item, "item");

        ItemStack key = standardize(item);
        return items.getOrDefault(key, 0L);
    }

    /**
     * Clears all stored items.
     */
    public synchronized void clear() {
        items.clear();
    }

    /**
     * Copies the contents of another storage into this storage.
     *
     * @param other The other storage
     */
    public synchronized void copyFrom(@NotNull ItemStorage other) {
        Objects.requireNonNull(other, "other");

        items.clear();
        other.items.forEach((key, value) -> {
            if (value <= 0) return;
            items.put(standardize(key), value);
        });
    }

    /**
     * Returns an immutable snapshot of the stored items.
     *
     * @return The snapshot map
     */
    public synchronized @NotNull Map<ItemStack, Long> asMap() {
        return Collections.unmodifiableMap(items.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().clone(), Map.Entry::getValue)));
    }

    /**
     * Returns the entries as a list sorted by item type then display name.
     *
     * @return The entry list
     */
    public synchronized @NotNull List<Map.Entry<ItemStack, Long>> asEntryList() {
        return items.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().clone(), entry.getValue()))
                .sorted(Comparator
                        .comparing((Map.Entry<ItemStack, Long> e) -> e.getKey().getType().getKey().getKey())
                        .thenComparing(e -> Optional.ofNullable(e.getKey().getItemMeta())
                                .map(meta -> meta.hasDisplayName() ? meta.displayName().toString() : "")
                                .orElse("")))
                .collect(Collectors.toList());
    }

    /**
     * Returns whether the storage is empty.
     *
     * @return {@code true} if empty
     */
    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }

    private static final class Serializer implements JsonSerializer<ItemStorage> {
        @Override
        public JsonElement serialize(ItemStorage storage, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            JsonArray contents = new JsonArray();

            storage.items.forEach((key, value) -> {
                ItemStackLongPair pair = new ItemStackLongPair(key.clone(), value);
                contents.add(context.serialize(pair));
            });

            object.add("contents", contents);
            return object;
        }
    }

    private static final class Deserializer implements JsonDeserializer<ItemStorage> {
        @Override
        public ItemStorage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) {
                return new ItemStorage();
            }

            JsonObject object = jsonElement.getAsJsonObject();
            ItemStorage storage = new ItemStorage();

            if (!object.has("contents") || !object.get("contents").isJsonArray()) {
                return storage;
            }

            JsonArray contents = object.get("contents").getAsJsonArray();
            for (JsonElement element : contents) {
                ItemStackLongPair pair = context.deserialize(element, ItemStackLongPair.class);
                if (pair == null) continue;
                ItemStack key = pair.getKey();
                long value = pair.getValue();
                if (key == null || value <= 0) continue;
                storage.addItem(key, value);
            }

            return storage;
        }
    }
}
