package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.RecipeDto;
import com.dishcovery.backend.interfaces.RecipeService;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Steps;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.Video;
import com.dishcovery.backend.repo.RecipeRepo;
import com.dishcovery.backend.repo.StepsRepo;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeServiceImple implements RecipeService {

    private final UserRepo userRepo;
    private final RecipeRepo recipeRepo;
    private final StepsRepo stepsRepo;

    public RecipeServiceImple(RecipeRepo recipeRepo, StepsRepo stepsReo, UserRepo userRepo) {
        this.recipeRepo = recipeRepo;
        this.stepsRepo = stepsReo;
        this.userRepo = userRepo;
    }

    @Override
    public Recipe uploadRecipe(RecipeDto recipeDto, Video video) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof  UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Users user = userRepo.findByUsername(userDetails.getUsername());

            // Set the recipe Data
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDto.getRecipeName());
            recipe.setCategory(recipeDto.getCategory());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setCookTime(recipeDto.getCookTime());
            recipe.setThumbnail(recipeDto.getThumbnail());
            recipe.setIngredients(recipeDto.getIngredients());
            if(video != null){
                recipe.setVideo(video);
            }

            // Reformat the steps in list and store in db
            List<String> stepsList = List.of(recipeDto.getSteps().split(","));
            Steps s = stepsRepo.save(new Steps(stepsList));
            recipe.setSteps(s);

            // Save the user
            recipe.setUser(user);


            return recipeRepo.save(recipe);
        }
        return null;

    }

    @Override
    public List<Recipe> getRecipeByUserId(int userId) {
        List<Recipe> recipes = recipeRepo.findAllByUserId(userId);
        return recipes;
    }

    @Override
    public Recipe getRecipeByVideoId(String videoId) {
        Recipe recipe = recipeRepo.findByVideoVideoId(videoId);
        return recipe;
    }

    @Override
    public Recipe getRecipeById(long recipeId) {
        return recipeRepo.findByRecipeId(recipeId);
    }

    @Override
    public Recipe updateRecipe(RecipeDto recipeDto, long recipeId) {
        System.out.println(recipeDto.toString());

        // Find existing recipe
        Recipe recipeDb = recipeRepo.findByRecipeId(recipeId);
        if (recipeDb == null) {
            throw new RuntimeException("Recipe not found with ID: " + recipeId);
        }

        // Update basic fields
        recipeDb.setRecipeName(recipeDto.getRecipeName());
        recipeDb.setCategory(recipeDto.getCategory());
        recipeDb.setDescription(recipeDto.getDescription());
        recipeDb.setCookTime(recipeDto.getCookTime());
        recipeDb.setThumbnail(recipeDto.getThumbnail());

        //Split new steps from DTO
        List<String> newSteps = new ArrayList<>(Arrays.asList(recipeDto.getSteps().split("\\r?\\n")));

        // Update steps entity
        Steps s = recipeDb.getSteps(); // current Steps entity
        if (s == null) {
            s = new Steps();
        }
        s.setSteps(newSteps); // update its list
        stepsRepo.save(s);

        // Reattach to recipe
        recipeDb.setSteps(s);

        // Save updated recipe
        return recipeRepo.save(recipeDb);
    }

}
