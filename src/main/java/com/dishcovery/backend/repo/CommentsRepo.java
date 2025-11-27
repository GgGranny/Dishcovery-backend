package com.dishcovery.backend.repo;


import com.dishcovery.backend.model.Comments;
import com.dishcovery.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long> {

    @Query("""
            SELECT c FROM Comments c
            WHERE c.recipe.recipeId = :recipeId
              AND c.parentComments IS NULL
              AND c.isDeleted = false
        """)
    List<Comments> findAllByRecipeAndParentCommentsIsNull(@Param("recipeId") Long recipeId);
}
