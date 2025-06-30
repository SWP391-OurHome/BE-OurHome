package com.javaweb.repository;

import com.javaweb.repository.entity.UserBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBookingRepository extends JpaRepository<UserBooking, Long> {
    @Modifying
    @Query("UPDATE UserBooking b SET b.viewed = true WHERE b.id = :id")
    void markAsViewed(@Param("id") Long id);

}