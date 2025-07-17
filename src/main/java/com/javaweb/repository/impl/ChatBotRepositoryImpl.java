package com.javaweb.repository.impl;

import com.javaweb.repository.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotRepositoryImpl extends JpaRepository<ChatEntity, Integer> {
    @Query("SELECT c FROM ChatEntity c WHERE " +
            "(c.sender.userId = :senderId AND c.receiver.userId = :receiverId) OR " +
            "(c.sender.userId = :receiverId AND c.receiver.userId = :senderId)")
    List<ChatEntity> findChatBetweenUsers(@Param("senderId") Integer senderId,
                                          @Param("receiverId") Integer receiverId);

}