package com.javaweb.repository.impl;


import com.javaweb.model.ContactDTO;
import com.javaweb.repository.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ContactRepositoryImpl extends JpaRepository<ContactEntity, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE ContactEntity c SET c.status = true WHERE c.contactId = :id")
    void markAsViewed(@Param("id") Integer id);

    @Query("SELECT new com.javaweb.model.ContactDTO(c.contactId, c.contactDate, c.status, c.response, " +
            "u.firstName, u.lastName, u.phone) " +
            "FROM ContactEntity c " +
            "JOIN c.property p " +
            "JOIN c.user u " +
            "WHERE p.user.id = :sellerUserId")
    List<ContactDTO> findContactsByPropertyOwnerId(@Param("sellerUserId") Integer sellerUserId);

    @Query("SELECT COUNT(c) FROM ContactEntity c WHERE c.property.user.id = :userId")
    int countByLandlordUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM ContactEntity c WHERE c.property.user.id = :userId")
    List<ContactEntity> findByPropertyUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(c) FROM ContactEntity c WHERE c.property.id = :propertyId")
    int countByPropertyId(@Param("propertyId") Integer propertyId);
}

