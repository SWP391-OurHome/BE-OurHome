package com.javaweb.repository.impl;

import com.javaweb.repository.entity.MembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepositoryImpl extends JpaRepository<MembershipEntity, Integer> {
    List<MembershipEntity> findByType(String type);
}