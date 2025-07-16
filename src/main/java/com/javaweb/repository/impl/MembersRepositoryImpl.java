package com.javaweb.repository.impl;

import com.javaweb.repository.entity.MembersEntity;
import com.javaweb.repository.entity.PackageMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepositoryImpl extends JpaRepository<MembersEntity, Integer> {
    MembersEntity findByUserUserIdAndMembershipMembershipId(Integer userId, Integer membershipId);
    MembersEntity findByUserUserId(Integer userId);

    @Query("SELECT m FROM MembersEntity m WHERE m.user.id = :userId AND m.status = true AND m.startDate <= CURRENT_DATE AND m.endDate >= CURRENT_DATE")
    MembersEntity findActiveByUserId(@Param("userId") Integer userId);
}
