package com.javaweb.controller;

import com.javaweb.model.UserBookingResponseDTO;
import com.javaweb.repository.entity.UserBooking;
import com.javaweb.service.UserBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")

public class UserBookingController {
@Autowired
    private  UserBookingService userBookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Integer> request) {
        int userId = request.get("userId");
        int propertyId = request.get("propertyId");

        UserBooking booking = userBookingService.createBooking(userId, propertyId);

        // Trả về object JSON có `success: true`
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", booking);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserBookingResponseDTO>> getAllBookings() {
        List<UserBookingResponseDTO> response = userBookingService.getAllBookings();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/view")
    public ResponseEntity<?> markBookingAsViewed(@PathVariable Long id) {
        userBookingService.markAsViewed(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

}