package com.javaweb.repository.impl;

import com.javaweb.repository.entity.FavouriteListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteListingRepositoryImpl extends JpaRepository<FavouriteListingEntity, Integer> {
    List<FavouriteListingEntity> findByUser_UserId(Integer userId);

    FavouriteListingEntity findByUser_UserIdAndProperty_Id(Integer userId, Integer propertyId);

    boolean existsByUser_UserIdAndProperty_Id(Integer userId, Integer propertyId);
}

