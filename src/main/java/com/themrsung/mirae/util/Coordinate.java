package com.themrsung.mirae.util;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * A coordinate.
 */
public final class Coordinate implements Serializable {
    private static final @NotNull Serializer SERIALIZER = new Serializer();
    private static final @NotNull Deserializer DESERIALIZER = new Deserializer();

    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    public static @NotNull JsonSerializer<Coordinate> serializer() {
        return SERIALIZER;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    public static @NotNull JsonDeserializer<Coordinate> deserializer() {
        return DESERIALIZER;
    }

    /**
     * Creates a new coordinate.
     *
     * @param world The world's name
     * @param x     The X value
     * @param y     The Y value
     * @param z     The Z value
     * @param yaw   The yaw
     * @param pitch The pitch
     */
    public Coordinate(@NotNull String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Creates a new coordinate.
     *
     * @param world The world's name
     * @param x     The X value
     * @param y     The Y value
     * @param z     The Z value
     */
    public Coordinate(@NotNull String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    /**
     * Creates a new coordinate.
     *
     * @param location The location
     */
    public Coordinate(@NotNull Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    private final @NotNull String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    /**
     * Returns this coordinate as a location.
     *
     * @return The location
     * @throws IllegalArgumentException When the world cannot be found
     */
    public @NotNull Location asLocation() throws IllegalArgumentException {
        World worldInstance = Bukkit.getWorld(world);

        if (worldInstance == null) {
            throw new IllegalArgumentException("Unable to find world " + world);
        }

        return new Location(worldInstance, x, y, z, yaw, pitch);
    }

    /**
     * Returns the world's name of this coordinate.
     *
     * @return The world's name of this coordinate
     */
    public @NotNull String world() {
        return world;
    }

    /**
     * Returns the X value of this coordinate.
     *
     * @return The X value of this coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Returns the Y value of this coordinate.
     *
     * @return The Y value of this coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Returns the Z value of this coordinate.
     *
     * @return The Z value of this coordinate
     */
    public double z() {
        return z;
    }

    /**
     * Returns the yaw of this coordinate.
     *
     * @return The yaw of this coordinate
     */
    public float yaw() {
        return yaw;
    }

    /**
     * Returns the pitch of this coordinate.
     *
     * @return The pitch of this coordinate
     */
    public float pitch() {
        return pitch;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Coordinate c)) return false;
        return Objects.equals(world, c.world) &&
                x == c.x &&
                y == c.y &&
                z == c.z &&
                yaw == c.yaw &&
                pitch == c.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "Coordinate[" +
                "world=" + world + ", " +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "z=" + z + ", " +
                "yaw=" + yaw + ", " +
                "pitch=" + pitch + ']';
    }

    /**
     * Serializer class.
     */
    private static final class Serializer implements JsonSerializer<Coordinate> {
        @Override
        public JsonElement serialize(Coordinate coordinate, Type type, JsonSerializationContext context) {
            if (coordinate == null) return JsonNull.INSTANCE;

            JsonObject object = new JsonObject();

            object.add("world", new JsonPrimitive(coordinate.world));
            object.add("x", new JsonPrimitive(coordinate.x));
            object.add("y", new JsonPrimitive(coordinate.y));
            object.add("z", new JsonPrimitive(coordinate.z));
            object.add("yaw", new JsonPrimitive(coordinate.yaw));
            object.add("pitch", new JsonPrimitive(coordinate.pitch));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    private static final class Deserializer implements JsonDeserializer<Coordinate> {
        @Override
        public Coordinate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;

            JsonObject object = jsonElement.getAsJsonObject();

            String world = null;
            double x = 0;
            double y = 0;
            double z = 0;

            if (!object.has("world") || object.get("world").isJsonNull()) {
                throw new JsonParseException("Missing or invalid required parameter \"world\".");
            }

            world = object.get("world").getAsString();

            if (!object.has("x") || !object.get("x").isJsonPrimitive()) {
                throw new JsonParseException("Missing or invalid required parameter \"x\".");
            }

            x = object.get("x").getAsDouble();

            if (!object.has("y") || !object.get("y").isJsonPrimitive()) {
                throw new JsonParseException("Missing or invalid required parameter \"y\".");
            }

            y = object.get("y").getAsDouble();

            if (!object.has("z") || !object.get("z").isJsonPrimitive()) {
                throw new JsonParseException("Missing or invalid required parameter \"z\".");
            }

            z = object.get("z").getAsDouble();

            float yaw = 0;
            float pitch = 0;

            if (object.has("yaw") && object.get("yaw").isJsonPrimitive()) {
                yaw = object.get("yaw").getAsFloat();
            }

            if (object.has("pitch") && object.get("pitch").isJsonPrimitive()) {
                pitch = object.get("pitch").getAsFloat();
            }

            return new Coordinate(world, x, y, z, yaw, pitch);
        }
    }
}
