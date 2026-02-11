package com.dishcovery.backend.service.admin;


import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.repo.RecipeRepo;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    /// boring stuff hei business logic ko lagi interface aafai banauney
    // admin ko rough xa time constrant ley garda

    @Autowired
    private RecipeRepo recipeRepo;

    @Autowired
    private UserRepo userRepo;

    // Feature the recipes
    public Recipe featureRecipe(Long recipeId, boolean status) {
        try{
            Recipe recipe = recipeRepo.findByRecipeId(recipeId);
            if(recipe == null) {
                throw new RuntimeException("Recipe not found to feature");
            }
            recipe.setFeatured(status);
            return recipeRepo.save(recipe);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
