package com.javaweb.repository;




import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.PropertyImageEnitity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImageEnitity, Long> {
    List<PropertyImageEnitity> findByProperty(PropertyEntity property);


    interface ListingRepository {
    }
}








