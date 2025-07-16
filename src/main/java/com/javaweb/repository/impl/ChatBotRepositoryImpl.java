package com.javaweb.repository.impl;

import com.javaweb.repository.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotRepositoryImpl extends JpaRepository<ChatEntity, Integer> {
    List<ChatEntity> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            Long senderId, Long receiverId, Long receiverId2, Long senderId2
    );
}