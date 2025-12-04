package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.LikeTypes;

public class LikesDto {
    private long id;
    private long commentId;
    private LikeTypes type;
    private String username;

    public LikesDto() {
    }

    public LikesDto(long id, long commentId, LikeTypes type, String username) {
        this.id = id;
        this.commentId = commentId;
        this.type = type;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return commentId;
    }

    public void setRecipeId(long recipeId) {
        this.commentId = recipeId;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public LikeTypes getType() {
        return type;
    }

    public void setType(LikeTypes type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LikesDto{" +
                "id=" + id +
                ", recipeId=" + commentId +
                ", type=" + type +
                '}';
    }
}
