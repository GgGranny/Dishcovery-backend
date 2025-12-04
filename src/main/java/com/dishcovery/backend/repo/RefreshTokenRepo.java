package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.RefreshToken;
import com.dishcovery.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken findByUser(Users u);

//    boolean existsByUserId(int userId);
//
//    RefreshToken findByUserId(int id);
}
