package com.javaweb.repository.impl;

import com.javaweb.repository.entity.NotificationEntity;
import com.javaweb.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepositoryImpl extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByUserOrderByTimestampDesc(UserEntity user);
    List<NotificationEntity> findByUserAndIsReadFalse(UserEntity user);
}
