package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.CommentDto;
import com.dishcovery.backend.dto.LikesDto;
import com.dishcovery.backend.model.Comments;

import java.util.List;

public interface CommentService {

    // Make a new comment or reply to a comment
    Comments addComment(CommentDto commentDto, String username);

    // Get all the comments for a specific video
    List<Comments> getAllComments(long recipeId);

    // Edit The Comment
    Comments updateComment(CommentDto commentDto, String username);

    // Delete A Comment
    boolean deleteComment(long commentId);

    // Like or Unlike a comment
    Comments likeOrDislike(LikesDto likesDto);

}
