
package com.javaweb.repository.impl;

import com.javaweb.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryImpl extends JpaRepository<RoleEntity, Integer> {
}
