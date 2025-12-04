package com.dishcovery.backend.model;


import com.dishcovery.backend.model.enums.LikeTypes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LikeTypes type;

    @ManyToOne
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JsonIgnore
    private Comments comments;

    public Likes() {
    }

    public Likes(Comments comments, Users user, LikeTypes type) {
        this.comments = comments;
        this.user = user;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LikeTypes getType() {
        return type;
    }

    public void setType(LikeTypes type) {
        this.type = type;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }
}
