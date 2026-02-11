package com.dishcovery.backend;


import com.dishcovery.backend.service.RecipeServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RecipeTest {

    @Autowired
    private RecipeServiceImple recipeServiceImple;

//    @Test
//    void checkRecipes(){
//        recipeServiceImple.getFeaturedRecipe();
//    }
}
