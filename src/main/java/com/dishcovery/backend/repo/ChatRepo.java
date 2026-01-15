package com.dishcovery.backend.repo;


import com.dishcovery.backend.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {
    @Query("SELECT chat FROM Chat  chat WHERE chat.community.id = ?1 ORDER BY chat.id DESC")
    Optional<List<Chat>> findAllByCommunity_Id(Long id);
}
