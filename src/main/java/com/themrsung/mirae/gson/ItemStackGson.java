package com.themrsung.mirae.gson;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

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
    static final class Serializer implements JsonSerializer<ItemStack> {
        @Override
        @SuppressWarnings("deprecation")
        public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(itemStack);
                dataOutput.close();

                String itemAsString = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                return new JsonPrimitive(itemAsString);
            } catch (IOException e) {
                throw new JsonParseException("Error serializing ItemStack.", e);
            }
        }
    }

    /**
     * Deserializer class.
     */
    static final class Deserializer implements JsonDeserializer<ItemStack> {
        @Override
        @SuppressWarnings("deprecation")
        public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                String itemAsString = jsonElement.getAsString();
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
