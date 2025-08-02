package com.themrsung.mirae.gson;

import com.google.gson.*;
import com.themrsung.mirae.skill.SkillType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * SkillType-long pair.
 */
public final class SkillTypeLongPair implements Serializable {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<SkillTypeLongPair> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<SkillTypeLongPair> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new pair.
     *
     * @param key   The key
     * @param value The value
     */
    public SkillTypeLongPair(@NotNull SkillType key, long value) {
        this.key = key;
        this.value = value;
    }

    private final @NotNull SkillType key;
    private final long value;

    /**
     * Returns the key.
     *
     * @return The key
     */
    public @NotNull SkillType getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    public long getValue() {
        return value;
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<SkillTypeLongPair> {
        @Override
        public JsonElement serialize(SkillTypeLongPair pair, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("key", context.serialize(pair.key));
            object.add("value", new JsonPrimitive(pair.value));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<SkillTypeLongPair> {
        @Override
        public SkillTypeLongPair deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();

            SkillType key = null;
            long value = 0;

            if (!object.has("key") || object.get("key").isJsonNull()) {
                throw new JsonParseException("Missing or invalid parameter \"key\"");
            }

            key = context.deserialize(object.get("key"), SkillType.class);

            if (!object.has("value") || object.get("value").isJsonNull()) {
                throw new JsonParseException("Missing or invalid parameter \"value\"");
            }

            value = object.get("value").getAsLong();

            return new SkillTypeLongPair(key, value);
        }
    }
}
