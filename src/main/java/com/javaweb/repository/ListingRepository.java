package com.javaweb.repository;


import com.javaweb.repository.entity.ListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface ListingRepository extends JpaRepository<ListingEntity, Integer> {
    Optional<ListingEntity> findByPropertyId(Integer propertyId);
}
