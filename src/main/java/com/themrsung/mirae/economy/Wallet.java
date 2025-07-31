package com.themrsung.mirae.economy;

import com.google.gson.*;
import com.themrsung.mirae.event.economy.EconomyCause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * A Mirae wallet.
 */
public interface Wallet extends Serializable {
    /**
     * Creates and returns a new wallet.
     *
     * @return The new wallet
     */
    static @NotNull Wallet createWallet() {
        return new SynchronizedWallet();
    }

    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    static @NotNull JsonSerializer<Wallet> serializer() {
        return Serializer.INSTANCE;
    }

    /**
     * Returns the deserializer instance.
     *
     * @return The deserializer instance
     */
    static @NotNull JsonDeserializer<Wallet> deserializer() {
        return Deserializer.INSTANCE;
    }

    /**
     * Returns the balance of this wallet.
     *
     * @return The balance of this wallet
     */
    double getBalance();

    /**
     * Modifies the balance of this wallet.
     *
     * @param change The net change to apply
     * @return The balance after
     */
    double modifyBalance(double change);

    /**
     * Modifies the balance of this wallet.
     *
     * @param change The net change to apply
     * @param cause  The cause of the change
     * @return The balance after
     */
    double modifyBalance(double change, @Nullable EconomyCause cause);

    /**
     * Modifies the balance of this wallet.
     *
     * @param change  The net change to apply
     * @param cause   The cause of the change
     * @param message The message
     * @return The balance after
     */
    double modifyBalance(double change, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Returns the coin balance of this wallet.
     *
     * @return The coin balance of this wallet
     */
    long getCoinBalance();

    /**
     * Modifies the coin balance of this wallet.
     *
     * @param change The net change to apply
     * @return The coin balance after
     */
    long modifyCoinBalance(long change);

    /**
     * Modifies the coin balance of this wallet.
     *
     * @param change The net change to apply
     * @param cause  The cause of the change
     * @return The coin balance after
     */
    long modifyCoinBalance(long change, @Nullable EconomyCause cause);

    /**
     * Modifies the coin balance of this wallet.
     *
     * @param change  The net change to apply
     * @param cause   The cause of the change
     * @param message The message
     * @return The coin balance after
     */
    long modifyCoinBalance(long change, @Nullable EconomyCause cause, @Nullable String message);

    /**
     * Serializer class.
     */
    class Serializer implements JsonSerializer<Wallet> {
        private static final @NotNull Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(Wallet wallet, Type type, JsonSerializationContext context) {
            if (wallet == null) return JsonNull.INSTANCE;

            JsonObject object = new JsonObject();

            object.add("balance", new JsonPrimitive(wallet.getBalance()));
            object.add("coinBalance", new JsonPrimitive(wallet.getCoinBalance()));

            return object;
        }
    }

    /**
     * Deserializer class.
     */
    class Deserializer implements JsonDeserializer<Wallet> {
        private static final @NotNull Deserializer INSTANCE = new Deserializer();

        private Deserializer() {
        }

        @Override
        public Wallet deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement == null || jsonElement.isJsonNull()) return null;
            JsonObject object = jsonElement.getAsJsonObject();

            if (!object.has("balance") || !object.get("balance").isJsonPrimitive()) {
                throw new JsonParseException("Invalid required parameter \"balance\".");
            }

            double balance = object.get("balance").getAsDouble();

            if (!object.has("coinBalance") || !object.get("coinBalance").isJsonPrimitive()) {
                throw new JsonParseException("Invalid required parameter \"coinBalance\".");
            }

            long coinBalance = object.get("coinBalance").getAsLong();

            return new SynchronizedWallet(balance, coinBalance);
        }
    }
}
