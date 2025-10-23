package com.dishcovery.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Video {
    @Id
    private String videoId;

    private String title;

    private String description;

    @Lob
    private String thumbnail;

    private String path;

    private String contentType;

    private LocalDateTime uploadedAt;


    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private Users user;

    public Video() {
    }

    public Video(String videoId, String title, String description, String thumbnail, String path, String contentType, Users user, LocalDateTime uploadedAt) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.path = path;
        this.contentType = contentType;
        this.user = user;
        this.uploadedAt = uploadedAt;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", path='" + path + '\'' +
                ", contentType='" + contentType + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", user=" + user +
                '}';
    }
}
