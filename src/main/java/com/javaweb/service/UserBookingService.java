package com.javaweb.service;

import com.javaweb.model.UserBookingResponseDTO;
import com.javaweb.repository.entity.UserBooking;

import java.util.List;

public interface UserBookingService {
    UserBooking createBooking(int userId, int propertyId);
    List<UserBookingResponseDTO> getAllBookings();
    public void markAsViewed(Long id);
}