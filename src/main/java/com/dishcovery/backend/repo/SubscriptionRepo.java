package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Subscription;
import com.dishcovery.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    Subscription findByUser_Id(int id);

    boolean existsByUser_Id(int userId);
}
