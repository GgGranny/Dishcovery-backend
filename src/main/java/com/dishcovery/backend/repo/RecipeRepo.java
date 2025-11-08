package com.dishcovery.backend.repo;


import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

    List<Recipe> user(Users user);

    List<Recipe> findAllByUserId(int userId);

    List<Recipe> video(Video video);

    Recipe findByVideoVideoId(String videoId);

    Recipe findByRecipeId(long recipeId);
}
