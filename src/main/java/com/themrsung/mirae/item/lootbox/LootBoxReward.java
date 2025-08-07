package com.themrsung.mirae.item.lootbox;

import com.themrsung.mirae.item.ItemSupplier;
import com.themrsung.mirae.item.ItemsAdderItem;
import com.themrsung.mirae.item.economy.Banknote;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A loot box reward.
 */
public interface LootBoxReward extends ItemSupplier {
    /**
     * Creates a new ItemStack-based reward.
     *
     * @param chances   The chances
     * @param itemStack The material
     * @param rarity    The rarity
     * @return The reward
     */
    static @NotNull LootBoxReward fromItem(long chances, @NotNull ItemStack itemStack, @NotNull Rarity rarity) {
        return new ItemStackBased(chances, itemStack, rarity);
    }

    /**
     * Creates a new Material-based reward.
     *
     * @param chances  The chances
     * @param material The material
     * @param quantity The quantity
     * @param rarity   The rarity
     * @return The reward
     */
    static @NotNull LootBoxReward fromItem(long chances, @NotNull Material material, int quantity, @NotNull Rarity rarity) {
        return new MaterialBased(chances, material, quantity, rarity);
    }

    /**
     * Creates a new ItemsAdder reward.
     *
     * @param chances    The chances
     * @param instanceId The ItemsAdder instance id
     * @param rarity     The rarity
     * @return The reward
     */
    static @NotNull LootBoxReward fromItemsAdder(long chances, @NotNull String instanceId, @NotNull Rarity rarity) {
        return new ItemsAdderBased(chances, instanceId, rarity);
    }

    /**
     * Creates a new banknote reward.
     *
     * @param chances The chances
     * @param money   The money
     * @param rarity  The rarity
     * @return The reward
     */
    static @NotNull LootBoxReward fromBanknote(long chances, double money, @NotNull Rarity rarity) {
        return new BanknoteBased(chances, money, rarity);
    }

    /**
     * Creates a new delegated reward.
     *
     * @param chances The chances
     * @param getter  The getter
     * @param rarity  The rarity
     * @return The reward
     */
    static @NotNull LootBoxReward fromSupplier(long chances, @NotNull ItemSupplier getter, @NotNull Rarity rarity) {
        return new DelegateBased(chances, getter, rarity);
    }

    /**
     * Returns the chances of this reward relative to the total denominator.
     *
     * @return The chances
     */
    long getChances();

    /**
     * Returns the rarity of this reward.
     *
     * @return The rarity
     */
    default @NotNull Rarity getRarity() {
        return Rarity.DEFAULT;
    }

    /**
     * The rarity of a reward.
     */
    enum Rarity {
        /**
         * Default rarity.
         */
        DEFAULT(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, false),

        /**
         * Rare item.
         */
        RARE(Sound.ENTITY_PLAYER_LEVELUP, false),

        /**
         * Legendary item.
         */
        LEGENDARY(Sound.UI_TOAST_CHALLENGE_COMPLETE, false);

        /**
         * Creates a new rarity.
         *
         * @param sound     The sound
         * @param broadcast Whether this should be broadcasted
         */
        Rarity(@NotNull Sound sound, boolean broadcast) {
            this.sound = sound;
            this.broadcast = broadcast;
        }

        private final @NotNull Sound sound;
        private final boolean broadcast;

        /**
         * Returns the sound this should make.
         *
         * @return The sound
         */
        public @NotNull Sound getSound() {
            return sound;
        }

        /**
         * Returns whether this should be broadcasted.
         *
         * @return {@code true} to broadcast
         */
        public boolean shouldBroadcast() {
            return broadcast;
        }
    }

    /**
     * ItemStack-based loot box reward.
     */
    class ItemStackBased implements LootBoxReward {
        /**
         * Private constructor.
         *
         * @param chances The chances
         * @param item    The item
         * @param rarity  The rarity
         */
        private ItemStackBased(long chances, @NotNull ItemStack item, @NotNull Rarity rarity) {
            this.chances = chances;
            this.item = item.clone();
            this.rarity = rarity;
        }

        private final long chances;
        private final @NotNull ItemStack item;
        private final @NotNull Rarity rarity;

        @Override
        public long getChances() {
            return chances;
        }

        @Override
        public @NotNull ItemStack getItem() {
            return item;
        }

        @Override
        public @NotNull Rarity getRarity() {
            return rarity;
        }
    }

    /**
     * A Material-based reward.
     */
    class MaterialBased implements LootBoxReward {
        /**
         * Private constructor.
         *
         * @param chances  The chances
         * @param type     The type
         * @param quantity The quantity
         * @param rarity   The rarity
         */
        private MaterialBased(long chances, @NotNull Material type, int quantity, @NotNull Rarity rarity) {
            this.chances = chances;
            this.type = type;
            this.quantity = quantity;
            this.rarity = rarity;
        }

        private final long chances;
        private final @NotNull Material type;
        private final int quantity;
        private final @NotNull Rarity rarity;

        @Override
        public long getChances() {
            return chances;
        }

        @Override
        public @NotNull ItemStack getItem() {
            return new ItemStack(type, quantity);
        }

        @Override
        public @NotNull Rarity getRarity() {
            return rarity;
        }
    }

    /**
     * ItemsAdder-based reward.
     */
    class ItemsAdderBased extends ItemsAdderItem implements LootBoxReward {
        /**
         * Private constructor.
         *
         * @param chances    The chances
         * @param instanceId The instance id
         * @param rarity     The rarity
         */
        private ItemsAdderBased(long chances, @NotNull String instanceId, @NotNull Rarity rarity) {
            super(instanceId);

            this.chances = chances;
            this.rarity = rarity;
        }

        private final long chances;
        private final @NotNull Rarity rarity;

        @Override
        public long getChances() {
            return chances;
        }

        @Override
        public @NotNull Rarity getRarity() {
            return rarity;
        }
    }

    /**
     * Banknote-based reward.
     */
    class BanknoteBased extends Banknote implements LootBoxReward {
        /**
         * Private constructor.
         *
         * @param chances      The chances
         * @param denomination The denomination
         * @param rarity       The rarity
         */
        private BanknoteBased(long chances, double denomination, @NotNull Rarity rarity) {
            super(denomination);

            this.chances = chances;
            this.rarity = rarity;
        }

        private final long chances;
        private final @NotNull Rarity rarity;

        @Override
        public long getChances() {
            return chances;
        }

        @Override
        public @NotNull Rarity getRarity() {
            return rarity;
        }
    }

    /**
     * Delegated reward.
     */
    class DelegateBased implements LootBoxReward {
        /**
         * Private constructor.
         *
         * @param chances The chances
         * @param getter  The getter
         * @param rarity  The rarity
         */
        private DelegateBased(long chances, @NotNull ItemSupplier getter, @NotNull Rarity rarity) {
            this.chances = chances;
            this.getter = getter;
            this.rarity = rarity;
        }

        private final long chances;
        private final @NotNull ItemSupplier getter;
        private final @NotNull Rarity rarity;

        @Override
        public long getChances() {
            return chances;
        }

        @Override
        public @NotNull ItemStack getItem() {
            return getter.getItem();
        }

        @Override
        public @NotNull Rarity getRarity() {
            return rarity;
        }
    }

}
