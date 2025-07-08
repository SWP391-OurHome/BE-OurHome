package com.javaweb.repository.impl;

import com.javaweb.repository.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
    boolean existsByProperty_IdAndUser_UserIdAndStatusIn(
            Integer propertyId, Integer userId, List<String> statusList);

    List<ReportEntity> findAllByOrderByReportDateDesc();
}