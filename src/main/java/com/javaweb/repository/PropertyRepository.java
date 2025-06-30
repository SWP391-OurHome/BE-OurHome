package com.javaweb.repository;

import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {
    public List<PropertyEntity> findAll();

    @Query("SELECT p FROM PropertyEntity p WHERE p.user.id = :userId")
    List<PropertyEntity> findByUserId(@Param("userId") Integer userId);
}
