package com.themrsung.mirae.banknote;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.item.economy.Banknote;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * Banknote utilities.
 */
public final class Banknotes {
    /**
     * Returns whether the given item is a banknote.
     *
     * @param item The item to check
     * @return The query result
     */
    public static @NotNull BanknoteQueryResult isBanknote(@Nullable ItemStack item) {
        if (item == null) return new BanknoteQueryResult(false);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return new BanknoteQueryResult(false);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        double amount = Objects.requireNonNullElse(container.get(key("mirae.banknote.amount"), PersistentDataType.DOUBLE), 0d);
        String signature = Objects.requireNonNullElse(container.get(key("mirae.banknote.signature"), PersistentDataType.STRING), "");
        Banknote.Version version;

        try {
            String versionRaw = Objects.requireNonNullElse(container.get(key("mirae.banknote.version"), PersistentDataType.STRING), "");
            version = Banknote.Version.valueOf(versionRaw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new BanknoteQueryResult(false);
        }

        Component displayName = displayName(amount, version);
        if (!Objects.equals(meta.displayName(), displayName))
            return new BanknoteQueryResult(false, version, Double.NaN);

        String dataToSign = amount + "|" + version + "|mirae";
        String signatureOnNote = sign(dataToSign, version.getPrivateKey());

        if (Objects.equals(signature, signatureOnNote)) {
            return new BanknoteQueryResult(true, version, amount);
        } else {
            return new BanknoteQueryResult(false, version, amount);
        }
    }

    /**
     * Returns namespaced key.
     *
     * @param key The key string
     * @return The key
     */
    public static @NotNull NamespacedKey key(@NotNull String key) {
        return new NamespacedKey(Mirae.getPlugin(), key);
    }

    /**
     * Signs the data using the secret key.
     *
     * @param data   The data to sign
     * @param secret The secret key
     * @return The signed data
     */
    public static @NotNull String sign(@NotNull String data, @NotNull String secret) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign data", e);
        }
    }

    /**
     * Custom denominations.
     */
    private static final @NotNull Map<Double, String> CUSTOM_DENOMINATIONS = Map.of(
            1000d, "천원",
            5000d, "오천원",
            10000d, "만원",
            50000d, "오만원",
            100000d, "십만원"
    );

    /**
     * Returns the valid display name for a banknote.
     *
     * @param denomination The denomination
     * @param version      The version
     * @return The valid display name
     */
    public static @NotNull Component displayName(double denomination, @NotNull Banknote.Version version) {
        return MiniMessage.miniMessage()
                .deserialize("<gradient:" + version.getColorStart() + ":"
                        + version.getColorEnd() + "><bold>"
                        + CUSTOM_DENOMINATIONS.getOrDefault(denomination, MX.formatBalance(denomination)) + "<reset>")
                .applyFallbackStyle(Style.style()
                        .decoration(TextDecoration.ITALIC, false)
                        .build());
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Banknotes() throws Exception {
        throw new Exception();
    }
}
