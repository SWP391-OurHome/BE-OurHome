package com.javaweb.service;

import com.javaweb.model.NotificationDTO;

import java.util.List;

public interface NotificationService {

    boolean sendAndSaveNotification(Integer userId, String type, String message, Integer relatedId);

    boolean notifyMemberExpired(Integer userId, Integer membershipId);

    boolean notifyUserBanned(Integer userId, String reason);

    boolean notifyPostBanned(Integer userId, Integer listingId, String reason);

    List<NotificationDTO> getNotificationsForUser(Integer userId);

    List<NotificationDTO> getUnreadNotificationsForUser(Integer userId);

    boolean markAsRead(Integer notificationId, Integer userId);
}