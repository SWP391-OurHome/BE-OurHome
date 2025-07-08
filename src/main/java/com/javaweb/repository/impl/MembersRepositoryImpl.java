package com.javaweb.repository.impl;

import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.PackageMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepositoryImpl extends JpaRepository<MembersEntity, Integer> {
    MembersEntity findByUserUserIdAndMembershipMembershipId(Integer userId, Integer membershipId);
    MembersEntity findByUserUserId(Integer userId);
}
