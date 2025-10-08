package com.themrsung.mirae.gson;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;

/**
 * {@link ItemStack} Gson serialization.
 */
public final class ItemStackGson {
    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance.
     */
    public static @NotNull JsonSerializer<ItemStack> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    public static @NotNull JsonDeserializer<ItemStack> deserializer() {
        return DESERIALIZER;
    }

    private static final Serializer SERIALIZER = new Serializer();
    private static final Deserializer DESERIALIZER = new Deserializer();

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<ItemStack> {
        @Override
        public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
            return context.serialize(item.serialize());
        }

        @Deprecated(forRemoval = true)
        private JsonElement legacySerialize(ItemStack item) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(item);
                dataOutput.close();

                String itemAsString = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                return new JsonPrimitive(itemAsString);
            } catch (IOException e) {
                return JsonNull.INSTANCE;
                // Added resilience (2025/09/04)
                // throw new JsonParseException("Error serializing ItemStack.", e);
            }
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<ItemStack> {
        @Override
        public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                Map<String, Object> map = context.deserialize(element, Map.class);
                return ItemStack.deserialize(map);
            } catch (RuntimeException e) {
                try {
                    return legacyDeserialize(element);
                } catch (JsonParseException e2) {
                    throw new JsonParseException(e2);
                }
            }
        }

        @Deprecated
        private ItemStack legacyDeserialize(JsonElement element) throws JsonParseException {
            // Added resilience (2025/09/04)
            if (element.isJsonNull()) {
                return ItemStack.of(Material.AIR);
            }

            try {
                String itemAsString = element.getAsString();
                byte[] data = Base64.getDecoder().decode(itemAsString);

                ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                ItemStack item = (ItemStack) dataInput.readObject();
                dataInput.close();

                return item;
            } catch (IOException | ClassNotFoundException e) {
                throw new JsonParseException("Error deserializing ItemStack.", e);
            }
        }
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private ItemStackGson() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
