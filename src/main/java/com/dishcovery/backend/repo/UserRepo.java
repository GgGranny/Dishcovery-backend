package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);

    Users findByUsername(String username);
}
