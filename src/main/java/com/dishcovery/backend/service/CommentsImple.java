package com.dishcovery.backend.service;

import com.dishcovery.backend.components.UserSession;
import com.dishcovery.backend.dto.CommentDto;
import com.dishcovery.backend.dto.LikesDto;
import com.dishcovery.backend.interfaces.CommentService;
import com.dishcovery.backend.model.Comments;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.CommentsRepo;
import com.dishcovery.backend.repo.RecipeRepo;
import com.dishcovery.backend.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CommentsImple implements CommentService {

    private UserRepo userRepo;
    private RecipeRepo recipeRepo;
    private CommentsRepo commentsRepo;

    public CommentsImple(UserRepo userRepo, RecipeRepo recipeRepo, CommentsRepo commentsRepo) {
        this.userRepo = userRepo;
        this.recipeRepo = recipeRepo;
        this.commentsRepo = commentsRepo;
    }

    @Override
    public Comments addComment(CommentDto commentDto, String username) {
        Comments comments = new Comments();
        comments.setCreatedAt(LocalDateTime.now());
        comments.setContent(commentDto.getContent());

        Recipe recipe = recipeRepo.findByRecipeId(commentDto.getRecipeId());
        comments.setRecipe(recipe);

        Users user = userRepo.findByUsername(username);
        comments.setUser(user);

        if(commentDto.getParentId() != null) {
            Comments parentComment = commentsRepo.findById(commentDto.getParentId())
                    .orElseThrow(()-> new RuntimeException("Parent comment not found"));
            comments.setParentComments(parentComment);
        }


        return commentsRepo.save(comments);
    }

    @Override
    public List<Comments> getAllComments(long recipeId) {
        Recipe recipe = recipeRepo.findByRecipeId(recipeId);
        List<Comments> allComments = commentsRepo.findAllByRecipeAndParentCommentsIsNull(recipeId);

        return allComments;
    }

    @Override
    public Comments updateComment(CommentDto commentDto, String username) {
        return null;
    }

    @Transactional
    @Override
    public boolean deleteComment(long commentId) {
        Comments comments = commentsRepo.findById(commentId).orElse(null);
        if(comments == null) {
            return false;
        }
        deleteReplies(comments);
        return true;
    }

    private void deleteReplies(Comments comment) {
        System.out.println("Hello");
        if (comment.getReplies() == null) return;
        System.out.println("there");
        // soft delete current comment
        comment.setDeleted(true);
        comment.setUpdateAt(LocalDateTime.now());
        commentsRepo.save(comment);

        // delete children
        if (comment.getReplies() != null) {
            for (Comments child : comment.getReplies()) {
                deleteReplies(child);
            }
        }
    }

    @Override
    public Comments likeOrDislike(LikesDto likesDto) {
        return null;
    }
}
