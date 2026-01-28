package com.dishcovery.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.User;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeId;

    private String recipeName;

    private String description;

    private String category;

    @Lob
    private String thumbnail;

    private String cookTime;

    private String ingredients;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_steps")
    private Steps steps;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="video_id")
    @JsonIgnore
    private Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    private boolean isFeatured = false;

    public Recipe() {
    }

    public Recipe(String recipeName, String description, String category, String thumbnail, String cookTime, Steps steps, Video video, Users user, String ingredients, boolean isFeatured) {
        this.recipeName = recipeName;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.cookTime = cookTime;
        this.steps = steps;
        this.video = video;
        this.user = user;
        this.ingredients = ingredients;
        this.isFeatured = isFeatured;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
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

    public Steps getSteps() {
        return steps;
    }

    public void setSteps(Steps steps) {
        this.steps = steps;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", steps=" + steps +", ingredients="+ ingredients+
                '}';
    }
}
