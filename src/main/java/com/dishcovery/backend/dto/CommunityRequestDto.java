package com.dishcovery.backend.dto;

import jakarta.persistence.Column;

public class CommunityRequestDto {
    private String communityName;
    private String description;
    private String category;
    private String tags;
    private boolean isPrivate;
    private String username;
    private int userId;

    public CommunityRequestDto() {
    }

    public CommunityRequestDto(String communityName, String description, String category, String tags, boolean isPrivate, String username, int userId) {
        this.communityName = communityName;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.isPrivate = isPrivate;
        this.username = username;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "CommunityRequestDto{" +
                "communityName='" + communityName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", tags='" + tags + '\'' +
                ", isPrivate=" + isPrivate +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }
}
