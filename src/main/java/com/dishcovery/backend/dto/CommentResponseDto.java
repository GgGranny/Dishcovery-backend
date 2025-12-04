package com.dishcovery.backend.dto;

import java.util.List;

public class CommentResponseDto {
    private Long id;
    private String content;
    private String createdAt;
    private String updatedAt;

    // User info (light version)
    private int userId;
    private String username;
    private String profilePicture;

    // Parent comment info (nullable)
    private Long parentId;

    private Long likesCount;

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    // Nested replies
    private List<CommentResponseDto> replies;

    public CommentResponseDto() {
    }

    public CommentResponseDto(List<CommentResponseDto> replies, Long parentId, String profilePicture, String username, int userId, String updatedAt, String createdAt, String content, Long likesCount) {
        this.replies = replies;
        this.parentId = parentId;
        this.profilePicture = profilePicture;
        this.username = username;
        this.userId = userId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.content = content;
        this.likesCount = likesCount;
    }

    public List<CommentResponseDto> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponseDto> replies) {
        this.replies = replies;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
