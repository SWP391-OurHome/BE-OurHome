package com.javaweb.repository.impl;


import com.javaweb.repository.entity.ChatEntity;
import com.javaweb.repository.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface ChatRepositoryImpl extends JpaRepository<ChatEntity, Integer> {


    @Query("SELECT c FROM ChatEntity c WHERE c.conversation.id = :conversationId " +
            "AND (c.sender.userId = :userId OR c.receiver.userId = :userId) " +
            "AND ((c.sender.userId = :otherUserId AND c.receiver.userId = :userId) " +
            "OR (c.sender.userId = :userId AND c.receiver.userId = :otherUserId))")
    List<ChatEntity> findByConversationIdAndUsers(@Param("conversationId") Integer conversationId,
                                                  @Param("userId") Integer userId,
                                                  @Param("otherUserId") Integer otherUserId);


    @Query("SELECT c FROM ChatEntity c WHERE (c.sender.userId = :userId1 AND c.receiver.userId = :userId2) " +
            "OR (c.sender.userId = :userId2 AND c.receiver.userId = :userId1)")
    List<ChatEntity> findMessagesBetweenUsers(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);


    @Query("SELECT DISTINCT c.conversation FROM ChatEntity c WHERE c.sender.userId = :userId OR c.receiver.userId = :userId")
    List<ConversationEntity> findConversationsByUserId(@Param("userId") Integer userId);
}



