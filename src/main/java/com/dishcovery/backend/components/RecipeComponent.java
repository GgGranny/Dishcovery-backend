package com.dishcovery.backend.components;

import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.repo.RecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeComponent {

    @Autowired
    private RecipeRepo recipeRepo;

    public Integer countRecipe(int userId){
        List<Recipe> recipes = recipeRepo.findAllByUserId(userId);
        if(recipes.isEmpty()) {
            return 0;
        }
        return recipes.size();
    }
}
