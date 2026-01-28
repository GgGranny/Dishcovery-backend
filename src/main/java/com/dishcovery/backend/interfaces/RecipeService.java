package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.RecipeDto;
import com.dishcovery.backend.dto.RecipeResponseDto;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.repo.RecipeRepo;

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

    // Get All Recipe
    List<Recipe> getAllRecipes(int pageNumber, int pageSize);

    List<Recipe> fetchSearchResult(String search, int pageNumber, int pageSize);

    List<Recipe> getRecommendation(Long recipeId);
}
