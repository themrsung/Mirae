package com.themrsung.mirae.item.lootbox;

import com.themrsung.mirae.item.ItemsAdderItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ItemsAdder-based custom loot box.
 */
public class ItemsAdderLootBox extends ItemsAdderItem implements LootBox {
    /**
     * Creates a new builder instance.
     *
     * @return The builder instance
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new items adder loot box.
     *
     * @param instanceId The instance id
     * @param rewards    The reward list
     */
    public ItemsAdderLootBox(@NotNull String instanceId, @NotNull Component displayName, @NotNull List<LootBoxReward> rewards) {
        super(instanceId);

        this.displayName = displayName;

        List<LootBoxReward> copy = List.copyOf(rewards);
        this.rewards = copy;
        this.denominator = copy.stream().mapToLong(LootBoxReward::getChances).sum();
    }

    /**
     * Copy constructor.
     *
     * @param box The box
     */
    public ItemsAdderLootBox(@NotNull ItemsAdderLootBox box) {
        super(box.instanceId);

        this.displayName = box.getDisplayName();
        this.rewards = box.getRewards();
        this.denominator = box.getTotalDenominator();
    }

    private final @NotNull Component displayName;
    private final @NotNull List<LootBoxReward> rewards;
    private final long denominator;

    @Override
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack box = super.getItem();
        ItemMeta meta = box.getItemMeta();

        meta.displayName(displayName);
        meta.lore(List.of(
                Component.text("우클릭하여 오픈합니다.").style(
                        Style.style()
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                                .color(TextColor.fromHexString("#5ac3ff")).build()
                )
        ));

        box.setItemMeta(meta);
        return box;
    }

    @Override
    public @NotNull List<LootBoxReward> getRewards() {
        return rewards;
    }

    @Override
    public double getProbabilityOf(@Nullable LootBoxReward reward) {
        if (reward == null || !rewards.contains(reward)) return 0;

        return (double) reward.getChances() / denominator;
    }

    @Override
    public long getTotalDenominator() {
        return denominator;
    }

    @Override
    public @NotNull LootBoxReward pollReward() {
        if (rewards.isEmpty()) {
            throw new RuntimeException("Loot box is empty. Cannot poll reward.");
        }

        int size = rewards.size();

        double[] probabilityMargins = new double[size];

        double cumulatedProbability = 0;
        for (int i = 0; i < size; i++) {
            LootBoxReward reward = rewards.get(i);

            double prob = (double) reward.getChances() / denominator;
            cumulatedProbability += prob;

            probabilityMargins[i] = cumulatedProbability;
        }

        Random random = new Random();
        double randomDouble = random.nextDouble();

        for (int i = 0; i < size; i++) {
            if (probabilityMargins[i] > randomDouble) {
                return rewards.get(i);
            }
        }

        return rewards.getLast();
    }

    /**
     * Builder class.
     */
    public static class Builder {
        private Builder() {
            this.instanceId = null;
            this.displayName = null;
            this.rewards = new ArrayList<>();
        }

        private String instanceId;
        private Component displayName;
        private @NotNull List<LootBoxReward> rewards;

        /**
         * Sets the instance id.
         *
         * @param id The id
         * @return {@code this}
         */
        public @NotNull Builder instanceId(@NotNull String id) {
            this.instanceId = id;
            return this;
        }

        public @NotNull Builder displayName(@NotNull Component displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the reward list.
         *
         * @param rewardList The list
         * @return {@code this}
         */
        public @NotNull Builder rewards(@NotNull List<LootBoxReward> rewardList) {
            rewards.addAll(rewardList);
            return this;
        }

        /**
         * Adds a reward.
         *
         * @param reward The reward to add
         * @return {@code this}
         */
        public @NotNull Builder reward(@NotNull LootBoxReward reward) {
            this.rewards.add(reward);
            return this;
        }

        /**
         * Clears the reward list.
         *
         * @return {@code this}
         */
        public @NotNull Builder clearRewards() {
            this.rewards.clear();
            return this;
        }

        /**
         * Builds the loot box.
         *
         * @return The loot box
         * @throws IllegalArgumentException When a required parameter is {@code null}
         */
        public @NotNull ItemsAdderLootBox build() {
            try {
                return new ItemsAdderLootBox(instanceId, displayName, rewards);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
