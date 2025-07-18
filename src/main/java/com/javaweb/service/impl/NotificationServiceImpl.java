package com.javaweb.service.impl;

import com.javaweb.model.NotificationDTO;
import com.javaweb.repository.entity.NotificationEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.NotificationRepositoryImpl;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepositoryImpl notificationRepository;

    @Autowired
    private UserRepositoryImpl userRepository; // For finding user by username

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public boolean sendAndSaveNotification(Integer userId, String type, String message, Integer relatedId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Save to DB
            NotificationEntity entity = new NotificationEntity();
            entity.setUser(user);
            entity.setType(type);
            entity.setMessage(message);
            entity.setRelatedId(relatedId);
            notificationRepository.save(entity);

            // Send via WebSocket using userId as principal name
            NotificationDTO dto = modelMapper.map(entity, NotificationDTO.class);
            messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/notifications", dto);

            return true;
        } catch (Exception e) {
            // Log error
            return false;
        }
    }

    public boolean notifyMemberExpired(Integer userId, Integer membershipId) {
        String message = "Your membership package has expired. Please renew now!";
        return sendAndSaveNotification(userId, "MEMBER_EXPIRED", message, membershipId);
    }

    public boolean notifyUserBanned(Integer userId, String reason) {
        String message = "Your account has been banned: " + reason;
        return sendAndSaveNotification(userId, "USER_BANNED", message, null);
    }

    public boolean notifyPostBanned(Integer userId, Integer listingId, String reason) {
        String message = "Your post has been banned: " + reason;
        return sendAndSaveNotification(userId, "POST_BANNED", message, listingId);
    }

    public List<NotificationDTO> getNotificationsForUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<NotificationEntity> entities = notificationRepository.findByUserOrderByTimestampDesc(user);
        return entities.stream()
                .map(entity -> modelMapper.map(entity, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotificationsForUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<NotificationEntity> entities = notificationRepository.findByUserAndIsReadFalse(user);
        return entities.stream()
                .map(entity -> modelMapper.map(entity, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean markAsRead(Integer notificationId, Integer userId) {
        try {
            NotificationEntity entity = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Notification not found"));
            if (!entity.getUser().getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }
            entity.setRead(true);
            notificationRepository.save(entity);
            return true;
        } catch (Exception e) {
            // Log error
            return false;
        }
    }
}