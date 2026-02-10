package com.dishcovery.backend.repo;

import com.dishcovery.backend.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {

}
