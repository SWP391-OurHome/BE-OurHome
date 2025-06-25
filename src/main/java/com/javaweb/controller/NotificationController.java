package com.javaweb.controller;

import com.javaweb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotificationToAllUsers(
            @RequestParam Long adminId,
            @RequestParam String title,
            @RequestParam String content) {
        try {
            notificationService.sendNotificationToAllUsers(adminId, title, content);
            return ResponseEntity.ok("Notification sent successfully to all users");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}