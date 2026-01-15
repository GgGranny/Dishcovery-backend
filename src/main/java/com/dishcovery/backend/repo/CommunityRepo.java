package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends JpaRepository<Community, Long> {
}
