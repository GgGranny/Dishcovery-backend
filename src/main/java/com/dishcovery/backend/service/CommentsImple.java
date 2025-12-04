package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.CommentDto;
import com.dishcovery.backend.dto.CommentResponseDto;
import com.dishcovery.backend.dto.LikesDto;
import com.dishcovery.backend.interfaces.CommentService;
import com.dishcovery.backend.model.*;
import com.dishcovery.backend.repo.CommentsLikesRepo;
import com.dishcovery.backend.repo.CommentsRepo;
import com.dishcovery.backend.repo.RecipeRepo;
import com.dishcovery.backend.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CommentsImple implements CommentService {

    private UserRepo userRepo;
    private RecipeRepo recipeRepo;
    private CommentsRepo commentsRepo;
    private CommentsLikesRepo commentsLikesRepo;
    private final Logger logger = LoggerFactory.getLogger(CommentsImple.class);

    public CommentsImple(UserRepo userRepo, RecipeRepo recipeRepo, CommentsRepo commentsRepo, CommentsLikesRepo commentsLikesRepo) {
        this.userRepo = userRepo;
        this.recipeRepo = recipeRepo;
        this.commentsRepo = commentsRepo;
        this.commentsLikesRepo = commentsLikesRepo;
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
    public List<CommentResponseDto> getAllComments(long recipeId) {
        Recipe recipe = recipeRepo.findByRecipeId(recipeId);
        List<Comments> allComments = commentsRepo.findAllByRecipeAndParentCommentsIsNull(recipeId);
        return prepareCommentsResponse(allComments);
    }

    private List<CommentResponseDto> prepareCommentsResponse(List<Comments> comments) {
        return comments.stream()
                .map(this::mapToDto)   // convert each comment entity → dto
                .toList();
    }
    private CommentResponseDto mapToDto(Comments comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt().toString());
        dto.setUpdatedAt(comment.getUpdateAt() != null ? comment.getUpdateAt().toString() : null);

        // user info
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setProfilePicture(comment.getUser().getProfilePicture());

        // parent
        dto.setParentId(
                comment.getParentComments() != null ? comment.getParentComments().getId() : null
        );

        //likes count
        dto.setLikesCount((long) comment.getLikes().size());

        // NESTED REPLIES → recursion
        List<CommentResponseDto> replyDtos = comment.getReplies()
                .stream()
                .map(this::mapToDto)
                .toList();

        dto.setReplies(replyDtos);

        return dto;
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
    public Likes likeOrDislike(  LikesDto likesDto) {
        Comments comments = commentsRepo.findById(likesDto.getCommentId()).orElse(null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Users usr = userRepo.findByUsername(username);
        if(comments == null) {
            logger.error("comments is null to be liked or disliked");
            throw new RuntimeException("Comment is empty");
        }
        Likes like = commentsLikesRepo.findByUserAndComments(usr, comments);
        if(like == null) {
            Likes newlikes = new Likes(comments, usr, likesDto.getType());
            return commentsLikesRepo.save(newlikes);
        }
        commentsLikesRepo.deleteById(like.getId());
        return like;
    }
}
