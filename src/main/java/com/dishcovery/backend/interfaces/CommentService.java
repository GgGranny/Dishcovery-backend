package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.CommentDto;
import com.dishcovery.backend.dto.CommentResponseDto;
import com.dishcovery.backend.dto.LikesDto;
import com.dishcovery.backend.model.Comments;
import com.dishcovery.backend.model.Likes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {

    // Make a new comment or reply to a comment
    Comments addComment(CommentDto commentDto, String username);

    // Get all the comments for a specific video
    List<CommentResponseDto> getAllComments(long recipeId);


    // Delete A Comment
    boolean deleteComment(long commentId);

    // Like or Unlike a comment
    Likes likeOrDislike( LikesDto likesDto);

}
