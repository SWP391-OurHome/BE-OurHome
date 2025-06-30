package com.javaweb.service.impl;

import com.javaweb.model.UserBookingResponseDTO;
import com.javaweb.repository.ListingRepository;
import com.javaweb.repository.PropertyRepository;
import com.javaweb.repository.UserBookingRepository;
import com.javaweb.repository.entity.ListingEntity;
import com.javaweb.repository.entity.PropertyEntity;
import com.javaweb.repository.entity.UserBooking;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.UserBookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookingServiceImpl implements UserBookingService {

    private final UserRepositoryImpl userRepository;
    private final PropertyRepository propertyRepository;
    private final ListingRepository listingRepository;
    private final UserBookingRepository userBookingRepository;

    @Override
    public UserBooking createBooking(int userId, int propertyId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));
        UserBooking booking = new UserBooking();
        booking.setUser(user);
        booking.setProperty(property);
        booking.setBookingTime(LocalDateTime.now());
        booking.setViewed(false);

        return userBookingRepository.save(booking);
    }
    public List<UserBookingResponseDTO> getAllBookings() {
        List<UserBooking> bookings = userBookingRepository.findAll();
        return bookings.stream()
                .map(b -> new UserBookingResponseDTO(
                        b.getId(),
                        b.getUser().getFirstName(),
                        b.getUser().getLastName(),
                        b.getUser().getEmail(),
                        b.getUser().getPhone(),
                        b.getViewed(),
                        b.getBookingTime() // 👈 Thêm dòng này
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public void markAsViewed(Long id) {
        userBookingRepository.markAsViewed(id);
    }

}
