package com.dishcovery.backend.dto;

public class RecipeDto {

    private String recipeName;

    private String description;

    private String category;

    private String thumbnail;

    private String cookTime;

    private String steps;

    private String videoId;

    private String ingredients;

    public RecipeDto() {
    }

    public RecipeDto(String recipeName, String description, String category, String thumbnail, String cookTime, String steps, String videoId, String ingredients) {
        this.recipeName = recipeName;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.cookTime = cookTime;
        this.steps = steps;
        this.videoId = videoId;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "recipeName='" + recipeName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", steps='" + steps + '\'' +
                ", videoId='" + videoId + '\'' +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }

    public String getVideoId() {
        return videoId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

}
