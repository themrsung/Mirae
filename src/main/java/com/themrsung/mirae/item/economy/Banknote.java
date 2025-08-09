package com.themrsung.mirae.item.economy;

import com.themrsung.mirae.banknote.Banknotes;
import com.themrsung.mirae.item.ItemsAdderItem;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/// //////////////////////////////////////////////////////////////////////////////////
/// ____    __    ____  ___      .______      .__   __.  __  .__   __.   _______  ///
/// \   \  /  \  /   / /   \     |   _  \     |  \ |  | |  | |  \ |  |  /  _____| ///
///  \   \/    \/   / /  ^  \    |  |_)  |    |   \|  | |  | |   \|  | |  |  __   ///
///   \            / /  /_\  \   |      /     |  . `  | |  | |  . `  | |  | |_ |  ///
///    \    /\    / /  _____  \  |  |\  \----.|  |\   | |  | |  |\   | |  |__| |  ///
///     \__/  \__/ /__/     \__\ | _| `._____||__| \__| |__| |__| \__|  \______|  ///
/// //////////////////////////////////////////////////////////////////////////////////
/// THIS FILE CONTAINS SECRET KEYS. DO NOT SHOW THIS TO A PUBLIC AUDIENCE!!!!!!!  ///
/// //////////////////////////////////////////////////////////////////////////////////

/**
 * A banknote item.
 */
public class Banknote extends ItemsAdderItem {

    /**
     * Creates a new banknote.
     *
     * @param denomination The denomination
     */
    public Banknote(double denomination) {
        this(denomination, Version.CURRENT);
    }

    /**
     * Creates a new banknote.
     *
     * @param denomination The denomination
     * @param version      The version
     */
    public Banknote(double denomination, @NotNull Version version) {
        super("iageneric:banknote");

        this.denomination = denomination;
        this.version = version;
    }

    private final double denomination;
    private final @NotNull Version version;

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack note = super.getItem();
        ItemMeta meta = note.getItemMeta();

        meta.displayName(Banknotes.displayName(denomination, version));

        String dataToSign = denomination + "|" + version + "|mirae";
        String signature = Banknotes.sign(dataToSign, version.getPrivateKey());

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(Banknotes.key("banknote.amount"), PersistentDataType.DOUBLE, denomination);
        container.set(Banknotes.key("banknote.signature"), PersistentDataType.STRING, signature);
        container.set(Banknotes.key("banknote.version"), PersistentDataType.STRING, version.toString());

        note.setItemMeta(meta);
        return note;
    }

    @Override
    public boolean isItem(@Nullable ItemStack item) {
        if (item == null) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        Component displayName = Banknotes.displayName(denomination, version);
        if (!Objects.equals(meta.displayName(), displayName)) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        double amount = Objects.requireNonNullElse(container.get(Banknotes.key("banknote.amount"), PersistentDataType.DOUBLE), 0d);
        String signature = Objects.requireNonNullElse(container.get(Banknotes.key("banknote.signature"), PersistentDataType.STRING), "");
        Version version;

        try {
            String versionRaw = Objects.requireNonNullElse(container.get(Banknotes.key("banknote.version"), PersistentDataType.STRING), "");
            version = Version.valueOf(versionRaw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        String dataToSign = amount + "|" + version + "|mirae";
        String signatureOnNote = Banknotes.sign(dataToSign, version.getPrivateKey());

        return Objects.equals(signature, signatureOnNote);
    }

    /**
     * Returns the denomination of this banknote.
     *
     * @return The denomination
     */
    public double getDenomination() {
        return denomination;
    }

    /**
     * Returns the version of this banknote.
     *
     * @return The version
     */
    public @NotNull Version getVersion() {
        return version;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /// ____    __    ____  ___      .______      .__   __.  __  .__   __.   _______  ///
    /// \   \  /  \  /   / /   \     |   _  \     |  \ |  | |  | |  \ |  |  /  _____| ///
    ///  \   \/    \/   / /  ^  \    |  |_)  |    |   \|  | |  | |   \|  | |  |  __   ///
    ///   \            / /  /_\  \   |      /     |  . `  | |  | |  . `  | |  | |_ |  ///
    ///    \    /\    / /  _____  \  |  |\  \----.|  |\   | |  | |  |\   | |  |__| |  ///
    ///     \__/  \__/ /__/     \__\ | _| `._____||__| \__| |__| |__| \__|  \______|  ///
    /////////////////////////////////////////////////////////////////////////////////////
    /// THIS FILE CONTAINS SECRET KEYS. DO NOT SHOW THIS TO A PUBLIC AUDIENCE!!!!!!!  ///
    /////////////////////////////////////////////////////////////////////////////////////

    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///

    /**
     * A banknote version.
     */
    public enum Version {
        /**
         * Block 0. Since @ v0.3.6
         */
        BLOCK_0("15fc7778-a3b0-443c-9c2c-d6b41d952d54", "#8df551", "#8df554", true);

        /**
         * The current version.
         */
        private static @NotNull Version CURRENT = BLOCK_0;

        /**
         * Creates a new banknote version.
         *
         * @param privateKey The key
         * @param colorStart The starting color
         * @param colorEnd   The ending color
         * @param valid      Whether this version is valid
         */
        Version(@NotNull String privateKey, @NotNull String colorStart, @NotNull String colorEnd, boolean valid) {
            this.privateKey = privateKey;
            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            this.valid = valid;
        }

        private final @NotNull String privateKey;
        private final @NotNull String colorStart;
        private final @NotNull String colorEnd;
        private final boolean valid;

        /**
         * Returns the private key.
         *
         * @return The private key
         */
        public @NotNull String getPrivateKey() {
            return privateKey;
        }

        /**
         * Returns the starting color.
         *
         * @return The starting color
         */
        public @NotNull String getColorStart() {
            return colorStart;
        }

        /**
         * Returns the ending color.
         *
         * @return The ending color
         */
        public @NotNull String getColorEnd() {
            return colorEnd;
        }

        /**
         * Returns whether the version is valid.
         *
         * @return {@code true} if valid
         */
        public boolean isValid() {
            return valid;
        }
    }
}
