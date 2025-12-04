package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Comments;
import com.dishcovery.backend.model.Likes;
import com.dishcovery.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsLikesRepo extends JpaRepository<Likes, Long> {

    Likes findByUserAndComments(Users user, Comments comments);
}
