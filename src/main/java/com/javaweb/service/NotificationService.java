package com.javaweb.service;

import com.javaweb.model.Notification;

public interface NotificationService {
    Notification sendNotificationToAllUsers(Long adminId, String title, String content);
}