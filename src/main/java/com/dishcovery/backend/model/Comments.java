package com.dishcovery.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne
    @JsonIgnoreProperties({"recipe","videos","comments", "password","email"})
    private Users user;

    @ManyToOne
    @JsonBackReference
    private Comments parentComments;

    @OneToMany(mappedBy = "parentComments", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comments> replies = new ArrayList<>();
//
//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
//    private List<Likes> likes;

    private int likesCount = 0;

    private boolean isDeleted = false;

    public Comments() {
    }

    public Comments(LocalDateTime createdAt, LocalDateTime updateAt, Recipe recipe, Users user, Comments parentComments, List<Comments> replies, int likesCount, boolean isDeleted, String content, List<Likes> likes) {
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.recipe = recipe;
        this.user = user;
        this.parentComments = parentComments;
        this.replies = replies;
        this.likesCount = likesCount;
        this.isDeleted = isDeleted;
        this.content = content;
//        this.likes = likes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Comments> getReplies() {
        return replies;
    }

    public void setReplies(List<Comments> replies) {
        this.replies = replies;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comments getParentComments() {
        return parentComments;
    }

    public void setParentComments(Comments parentComments) {
        this.parentComments = parentComments;
    }

//    public List<Likes> getLikes() {
//        return likes;
//    }
//
//    public void setLikes(List<Likes> likes) {
//        this.likes = likes;
//    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                ", likesCount=" + likesCount +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
