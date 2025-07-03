package com.javaweb.repository.impl;

import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<ListingEntity, Long> {
    Optional<ListingEntity> findByPropertyId(Integer propertyId);
    List<ListingEntity> findByPropertyIdIn(List<Integer> propertyIds);


}
