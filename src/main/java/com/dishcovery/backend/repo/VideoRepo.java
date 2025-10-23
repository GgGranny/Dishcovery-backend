package com.dishcovery.backend.repo;


import com.dishcovery.backend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepo extends JpaRepository<Video, String> {

    Optional<Video> findByVideoId(String id);
}
