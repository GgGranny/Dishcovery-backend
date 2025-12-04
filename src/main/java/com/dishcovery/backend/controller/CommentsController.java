package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.CommentDto;
import com.dishcovery.backend.dto.CommentResponseDto;
import com.dishcovery.backend.dto.LikesDto;
import com.dishcovery.backend.model.Comments;
import com.dishcovery.backend.model.Likes;
import com.dishcovery.backend.model.UsersPrincipal;
import com.dishcovery.backend.response.MyResponseHandler;
import com.dishcovery.backend.service.CommentsImple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/comments/c1")
public class CommentsController {

    private final CommentsImple commentsService;

    public CommentsController(CommentsImple commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/comment")
    public ResponseEntity<Comments> makeComment(
            @RequestParam(name="recipeId") Long recipeId,
            @RequestParam(name ="content") String content,
            @RequestParam(name = "parentId", required = false) Long parentId,
            @RequestParam(name="username") String username
    ) {
        CommentDto commentDto = new CommentDto();
        commentDto.setRecipeId(recipeId);
        commentDto.setContent(content);
        commentDto.setParentId(parentId);
        Comments comments = commentsService.addComment(commentDto, username);
        if(comments != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(comments);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/comment/{recipeId}")
    public ResponseEntity<List<?>> getComments(@PathVariable("recipeId") long recipeId) {
        List<CommentResponseDto> allComments = commentsService.getAllComments(recipeId);
        return ResponseEntity.status(HttpStatus.OK).body(allComments);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") long commentId) {
        boolean isDeleted = commentsService.deleteComment(commentId);
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK).body("comment deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to delete comment");
    }

    @PostMapping("/comment/like")
    public ResponseEntity<?> likeCommentOrDislike(@RequestBody LikesDto likesDto) {
        Likes likes = commentsService.likeOrDislike( likesDto);
        if(likes != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "action performed  successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.badRequest().body("failed to perform the action");
    }
}
