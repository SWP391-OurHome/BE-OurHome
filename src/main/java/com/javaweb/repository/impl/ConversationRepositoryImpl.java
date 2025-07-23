package com.javaweb.repository.impl;


import com.javaweb.repository.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface ConversationRepositoryImpl extends JpaRepository<ConversationEntity, Integer> {
    @Query("SELECT c FROM ConversationEntity c WHERE " +
            "((c.user1.userId = :userId1 AND c.user2.userId = :userId2) " +
            "OR (c.user1.userId = :userId2 AND c.user2.userId = :userId1))")
    Optional<ConversationEntity> findByUsers(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);
}



