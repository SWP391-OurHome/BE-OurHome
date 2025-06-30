package com.javaweb.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_bookings")
public class UserBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

    private LocalDateTime bookingTime;

    @Column(name = "viewed")
    private Boolean viewed; // true = ĐÃ XEM, false = CHƯA XEM, null = chưa được gán

// Giá trị: "CHƯA XEM", "ĐÃ XEM"

    // Getters and Setters
}