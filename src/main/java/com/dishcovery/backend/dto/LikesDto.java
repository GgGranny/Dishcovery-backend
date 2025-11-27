package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.LikeTypes;

public class LikesDto {
    private long id;
    private long recipeId;
    private LikeTypes type;

    public LikesDto() {
    }

    public LikesDto(long id, long recipeId, LikeTypes type) {
        this.id = id;
        this.recipeId = recipeId;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public LikeTypes getType() {
        return type;
    }

    public void setType(LikeTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LikesDto{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", type=" + type +
                '}';
    }
}
