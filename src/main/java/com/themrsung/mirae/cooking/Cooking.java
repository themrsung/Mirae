package com.themrsung.mirae.cooking;

import com.themrsung.mirae.cooking.debug.TestCookingRecipe;
import com.themrsung.mirae.cooking.recipe.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Cooking recipes.
 */
public final class Cooking {
    private static final @NotNull Set<CookingRecipe> RECIPES = Set.of(
            new TestCookingRecipe(),
            new SaltBreadRecipe(),
            new SausageRecipe(),
            new HamRecipe(),
            new ApplePieRecipe(),
            new ArmyStewRecipe(),
            new GamjaTangRecipe(),
            new SaladRecipe(),
            new OpiumRecipe(),
            new MorphineRecipe(),
            new CocaPasteRecipe(),
            new CocaineRecipe(),
            new AdvancedSteakRecipe(),
            new ScrambledEggRecipe()
    );

    /**
     * Returns the set of recipes.
     *
     * @return The set of recipes
     */
    public static @NotNull Set<CookingRecipe> getRecipes() {
        return RECIPES;
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Cooking() throws Exception {
        throw new Exception("Cannot instantiate.");
    }
}
