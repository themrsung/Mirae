package com.themrsung.mirae.gson;

import com.google.gson.*;
import com.themrsung.mirae.state.State;
import com.themrsung.mirae.util.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
     * Creates a new data object.
     *
     * @param state The state
     */
    public StateData(@NotNull State state) {
        this.spawnPoint = state.getSpawnPoint() != null ? new Coordinate(state.getSpawnPoint()) : null;

        Map<String, Coordinate> warps = new HashMap<>();
        state.getWarpMap().forEach((k, v) -> warps.put(k, new Coordinate(v)));

        this.warpMap = Map.copyOf(warps);
        this.trackedBanknoteIssuance = state.getTrackedBanknoteIssuance();
    }

    /**
     * Private constructor.
     */
    private StateData() {
        this.spawnPoint = null;
        this.warpMap = new HashMap<>();
        this.trackedBanknoteIssuance = 0;
    }

    private @Nullable Coordinate spawnPoint;
    private final @NotNull Map<String, Coordinate> warpMap;
    private double trackedBanknoteIssuance;

    /**
     * Returns the spawn point.
     *
     * @return The spawn point
     */
    public @Nullable Coordinate getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Returns the warp map.
     *
     * @return The warp map
     */
    public @NotNull Map<String, Coordinate> getWarpMap() {
        return Map.copyOf(warpMap);
    }

    /**
     * Returns the tracked banknote issuance.
     *
     * @return The tracked banknote issuance
     */
    public double getTrackedBanknoteIssuance() {
        return trackedBanknoteIssuance;
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<StateData> {
        @Override
        public JsonElement serialize(StateData data, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            object.add("spawnPoint", context.serialize(data.spawnPoint));

            JsonArray warps = new JsonArray();
            data.warpMap.forEach((k, v) -> {
                StringCoordinatePair pair = new StringCoordinatePair(k, v);
                warps.add(context.serialize(pair));
            });
            object.add("warps", warps);

            object.addProperty("trackedBanknoteIssuance", data.trackedBanknoteIssuance);

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
            StateData data = new StateData();

            if (object.has("spawnPoint") && !object.get("spawnPoint").isJsonNull()) {
                data.spawnPoint = context.deserialize(object.get("spawnPoint"), Coordinate.class);
            }

            if (object.has("warps") && object.get("warps").isJsonArray()) {
                JsonArray warps = object.get("warps").getAsJsonArray();
                warps.forEach(warp -> {
                    StringCoordinatePair pair = context.deserialize(warp, StringCoordinatePair.class);
                    data.warpMap.put(pair.getKey(), pair.getValue());
                });
            }

            if (object.has("trackedBanknoteIssuance") && object.get("trackedBanknoteIssuance").isJsonPrimitive()) {
                data.trackedBanknoteIssuance = object.get("trackedBanknoteIssuance").getAsDouble();
            }

            return data;
        }
    }
}
