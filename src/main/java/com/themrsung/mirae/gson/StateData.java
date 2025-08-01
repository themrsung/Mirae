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

    public static @NotNull JsonSerializer<StateData> serializer() {
        return SERIALIZER;
    }

    public static @NotNull JsonDeserializer<StateData> deserializer() {
        return DESERIALIZER;
    }

    public StateData(@Nullable Coordinate spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    private final @Nullable Coordinate spawnPoint;

    public @Nullable Coordinate getSpawnPoint() {
        return spawnPoint;
    }

    private static final class Serializer implements JsonSerializer<StateData> {
        @Override
        public JsonElement serialize(StateData pair, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("spawnPoint", context.serialize(pair.spawnPoint));

            return object;
        }
    }

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
