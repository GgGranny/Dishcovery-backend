package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.Users;


public class VideoDto {

    private String videoId;

    private String title;

    private String description;

    private String path;

    private String contentType;

    private Users user;

    public VideoDto() {
    }

    public VideoDto(Users user, String contentType, String path, String description, String title, String videoId) {
        this.user = user;
        this.contentType = contentType;
        this.path = path;
        this.description = description;
        this.title = title;
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VideoDto{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='"  + '\'' +
                ", path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", user=" + user +
                '}';
    }
}
