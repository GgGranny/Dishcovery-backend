package com.dishcovery.backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String communityName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    private String tags;

    @Column(nullable = false)
    private boolean isPrivate;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Chat> chat;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Users owner;

    public Community() {
    }

    public Community(String communityName, String description, String category, String tags, boolean isPrivate, List<Chat> chat, Users owner, LocalDateTime createdAt) {
        this.communityName = communityName;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.isPrivate = isPrivate;
        this.chat = chat;
        this.owner = owner;
        this.createdAt = createdAt;
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

    public List<Chat> getChat() {
        return chat;
    }

    public void setChat(List<Chat> chat) {
        this.chat = chat;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Community{" +
                "tags='" + tags + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", communityName='" + communityName + '\'' +
                ", id=" + id +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
