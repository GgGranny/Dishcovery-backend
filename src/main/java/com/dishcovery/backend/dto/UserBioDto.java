package com.dishcovery.backend.dto;

import java.util.List;

public class UserBioDto {
    private String displayName;

    private String bio;

    private String cuisinePreferences;

    public UserBioDto() {
    }

    public UserBioDto(String displayName, String bio, String cuisinePreferences) {
        this.displayName = displayName;
        this.bio = bio;
        this.cuisinePreferences = cuisinePreferences;
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

    public String getCuisinePreferences() {
        return cuisinePreferences;
    }

    public void setCuisinePreferences(String cuisinePreferences) {
        this.cuisinePreferences = cuisinePreferences;
    }

    @Override
    public String toString() {
        return "UserBioDto{" +
                 + '\'' +
                ", displayName='" + displayName + '\'' +
                ", bio='" + bio + '\'' +
                ", cuisinePreferences=" + cuisinePreferences +
                '}';
    }
}
