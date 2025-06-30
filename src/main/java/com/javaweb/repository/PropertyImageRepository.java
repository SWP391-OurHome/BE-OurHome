package com.javaweb.repository;


import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByProperty(PropertyEntity property);


    interface ListingRepository {
    }
}








