package com.javaweb.controller;

import com.javaweb.model.NotificationDTO;
import com.javaweb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // GET /api/notifications?userId=123
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@RequestParam Integer userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // GET /api/notifications/unread?userId=123
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@RequestParam Integer userId) {
        List<NotificationDTO> unread = notificationService.getUnreadNotificationsForUser(userId);
        return ResponseEntity.ok(unread);
    }

    // POST /api/notifications/5/read?userId=123
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer id, @RequestParam Integer userId) {
        boolean success = notificationService.markAsRead(id, userId);
        if (!success) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Failed to mark notification as read");
        }
        return ResponseEntity.ok().build();
    }
}
