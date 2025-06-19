package com.javaweb.repository;


import com.javaweb.repository.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer> {


    @Query("""
   SELECT p FROM PropertyEntity p
   WHERE (
       (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))
        OR LOWER(p.addressLine1) LIKE LOWER(CONCAT('%', :city, '%'))
        OR LOWER(p.addressLine2) LIKE LOWER(CONCAT('%', :city, '%'))
        OR LOWER(p.region) LIKE LOWER(CONCAT('%', :city, '%')))
      
       AND (:propertyType IS NULL OR LOWER(p.propertyType) LIKE LOWER(CONCAT('%', :propertyType, '%')))
      
       AND (:minPrice IS NULL OR CAST(p.price AS double) >= :minPrice)
       AND (:maxPrice IS NULL OR CAST(p.price AS double) <= :maxPrice)
      
       AND (:minArea IS NULL OR p.area >= :minArea)
       AND (:maxArea IS NULL OR p.area <= :maxArea)
      
       AND (:bedrooms IS NULL OR p.numBedroom = :bedrooms)
       AND (:bathrooms IS NULL OR p.numBathroom = :bathrooms)
   )
""")
    List<PropertyEntity> search(
            @Param("city") String city,
            @Param("propertyType") String propertyType,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            @Param("bedrooms") Integer bedrooms,
            @Param("bathrooms") Integer bathrooms
    );
}
