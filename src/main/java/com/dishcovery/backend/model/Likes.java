package com.dishcovery.backend.model;


import com.dishcovery.backend.model.enums.LikeTypes;
import jakarta.persistence.*;

@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LikeTypes type;

//    @ManyToOne
//    private Users user;
//
//    @ManyToOne
//    private Comments comments;
}
