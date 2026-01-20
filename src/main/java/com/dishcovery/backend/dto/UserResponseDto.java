package com.dishcovery.backend.dto;

import jakarta.persistence.*;

import java.util.List;

public class UserResponseDto {

    private int id;

    private String username;

    private String email;

    private String profilePicture;

    private String role;

    private boolean enabled;

    private String displayName;

    private String bio;

    private Integer recipeCount ;

    private Integer followersCount;

    private List<String> cuisinePreferences;

    public UserResponseDto() {
    }

    public UserResponseDto(int id, String username, String email, String profilePicture, String role, boolean enabled, String displayName, String bio, Integer recipeCount, Integer followersCount, List<String> cuisinePreferences) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.role = role;
        this.enabled = enabled;
        this.displayName = displayName;
        this.bio = bio;
        this.recipeCount = recipeCount;
        this.followersCount = followersCount;
        this.cuisinePreferences = cuisinePreferences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(Integer recipeCount) {
        this.recipeCount = recipeCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public List<String> getCuisinePreferences() {
        return cuisinePreferences;
    }

    public void setCuisinePreferences(List<String> cuisinePreferences) {
        this.cuisinePreferences = cuisinePreferences;
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", displayName='" + displayName + '\'' +
                ", bio='" + bio + '\'' +
                ", recipeCount=" + recipeCount +
                ", followersCount=" + followersCount +
                ", cuisinePreferences=" + cuisinePreferences +
                '}';
    }
}
