package com.dishcovery.backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "confirmAt")
    private LocalDateTime confirmAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Token() {
    }

    public Token(Users user,String token, LocalDateTime expiresAt,  LocalDateTime createdAt) {
        this.user = user;
        this.expiresAt = expiresAt;
        this.token = token;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(LocalDateTime confirmAt) {
        this.confirmAt = confirmAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", confirmAt=" + confirmAt +
                ", user=" + user +
                '}';
    }
}
