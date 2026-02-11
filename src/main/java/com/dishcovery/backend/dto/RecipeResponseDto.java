package com.dishcovery.backend.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.List;

public class RecipeResponseDto {
    //recipes
    private Long recipeId;

    private String recipeName;

    private String description;

    private String category;

    private String thumbnail;

    private String cookTime;

    private List<String> steps;

    private String videoId;

    private String ingredients;

    private LocalDateTime createdAt;

    private boolean isFeatured;

    //User
    private int userid;

    private String username;

    private String email;

    private String profilePicture;

    public RecipeResponseDto() {
    }

    public RecipeResponseDto(Long recipeId, String recipeName, String description, String category, String thumbnail, String cookTime, List<String> steps, String videoId, String ingredients, int userid, String username, String email, String profilePicture, LocalDateTime createdAt, boolean isFeatured ) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.cookTime = cookTime;
        this.steps = steps;
        this.videoId = videoId;
        this.ingredients = ingredients;
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.isFeatured = isFeatured;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
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

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
