package com.themrsung.mirae.gson;

import com.google.gson.*;
import com.themrsung.mirae.util.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * State data.
 */
public final class StateData implements Serializable {
    private static final @NotNull StateData.Serializer SERIALIZER = new StateData.Serializer();
    private static final @NotNull StateData.Deserializer DESERIALIZER = new StateData.Deserializer();

    /**
     * Returns the serializer.
     *
     * @return The serializer
     */
    public static @NotNull JsonSerializer<StateData> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer.
     *
     * @return The deserializer
     */
    public static @NotNull JsonDeserializer<StateData> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new state data object.
     *
     * @param spawnPoint The spawn point
     */
    public StateData(@Nullable Coordinate spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    private final @Nullable Coordinate spawnPoint;

    /**
     * Returns the spawn point.
     *
     * @return The spawn point
     */
    public @Nullable Coordinate getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<StateData> {
        @Override
        public JsonElement serialize(StateData pair, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("spawnPoint", context.serialize(pair.spawnPoint));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<StateData> {
        @Override
        public StateData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();

            Coordinate spawnPoint = null;

            if (object.has("spawnPoint") && !object.get("spawnPoint").isJsonNull()) {
                spawnPoint = context.deserialize(object.get("spawnPoint"), Coordinate.class);
            }

            return new StateData(spawnPoint);
        }
    }
}
