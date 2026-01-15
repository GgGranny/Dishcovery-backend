package com.dishcovery.backend.dto;

import java.time.LocalDateTime;

public class CommunityResponseDto {
    private Long id;
    private String communityName;
    private String description;
    private String category;
    private String tags;
    private boolean isPrivate;
    private String username;
    private int userId;
    private LocalDateTime createdAt;
    private String userProfile;

    public CommunityResponseDto() {
    }

    public CommunityResponseDto(String communityName, String description, String category, String tags, boolean isPrivate, String username, int userId, LocalDateTime createdAt, String userProfile) {
        this.communityName = communityName;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.isPrivate = isPrivate;
        this.username = username;
        this.userId = userId;
        this.createdAt = createdAt;
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
