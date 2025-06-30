package com.javaweb.repository;

import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {
    List<PropertyEntity> findByUserId(Integer userId); // <-- cần có dòng này


}
