package com.javaweb.repository.impl;

import com.javaweb.repository.entity.PackageMemberEntity;
import com.javaweb.repository.entity.PackageMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageMemberRepositoryImpl extends JpaRepository<PackageMemberEntity, PackageMemberId> {
}