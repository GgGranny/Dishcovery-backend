package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);

    Users findByUsername(String username);

    @Query("SELECT u FROM Users u WHERE  u.enabled = true AND u.role = 'USER'")
    List<Users> findAllByEnabled();
}
