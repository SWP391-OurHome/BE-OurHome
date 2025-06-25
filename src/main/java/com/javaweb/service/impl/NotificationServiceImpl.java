package com.javaweb.service.impl;

import com.javaweb.model.Notification;
import com.javaweb.repository.NotificationRepository;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Notification sendNotificationToAllUsers(Long adminId, String title, String content) {
        // Lấy admin
        UserEntity admin = userRepository.findById(adminId.intValue())
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        // Lấy tất cả người dùng (trừ admin nếu cần)
        List<UserEntity> users = userRepository.findAll()
                .stream()
                .filter(user -> !user.getUserId().equals(adminId.intValue()))
                .toList();

        // Tạo thông báo
        Notification notification = new Notification();
        notification.setAdmin(admin);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setUsers(users);

        // Lưu thông báo vào database
        Notification savedNotification = notificationRepository.save(notification);

        // Gửi email cho tất cả người dùng
        for (UserEntity user : users) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(title);
            message.setText(content + "\nSent by Admin: " + admin.getEmail());
            mailSender.send(message);
        }

        // Cập nhật trạng thái thành "SENT"
        savedNotification.setStatus("SENT");
        return notificationRepository.save(savedNotification);
    }
}