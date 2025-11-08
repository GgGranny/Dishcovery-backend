package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.RecipeDto;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Video;

import java.util.List;

public interface RecipeService {

    // Upload new recipe
    Recipe uploadRecipe(RecipeDto recipeDto, Video videoId);

    // Get all the recipe of the specific user
    List<Recipe> getRecipeByUserId(int userId);

    // Get recipe by video
    Recipe getRecipeByVideoId(String videoId);

    // Get recipe by id
    Recipe getRecipeById(long recipeId);

    // Update a Recipe
    Recipe updateRecipe(RecipeDto recipe, long recipeId);
}
