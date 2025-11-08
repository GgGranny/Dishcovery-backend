package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepsRepo extends JpaRepository<Steps, Long> {
    Steps findByStepsId(Steps steps);
}
