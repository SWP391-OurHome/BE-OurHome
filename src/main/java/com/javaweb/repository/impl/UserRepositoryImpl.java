package com.javaweb.repository.impl;

import com.javaweb.model.UserDTO;
import com.javaweb.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
