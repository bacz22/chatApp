package com.bac.chatApp.service.impl;

import com.bac.chatApp.dto.response.notification.NotificationResponse;
import com.bac.chatApp.exception.AppException;
import com.bac.chatApp.exception.ErrorCode;
import com.bac.chatApp.model.Notification;
import com.bac.chatApp.model.NotificationType;
import com.bac.chatApp.repository.NotificationRepository;
import com.bac.chatApp.repository.UserRepository;
import com.bac.chatApp.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendFriendRequestNotification(Long targetUserId, Long senderId, Long friendshipId) {
        //luu vao database
        Notification notification = Notification.builder()
                .userId(targetUserId)
                .senderId(senderId)
                .type(NotificationType.FRIEND_REQUEST)
                .friendshipId(friendshipId)
                .isRead(false)
                .build();
        notification = notificationRepository.save(notification);

        //gui realtime qua websocket
        NotificationResponse response = mapToResponse(notification);
        messagingTemplate.convertAndSendToUser(
                targetUserId.toString(),
                "/queue/notifications",
                response
        );
    }

    @Override
    public void sendFriendAcceptedNotification(Long targetUserId, Long senderId, Long friendshipId) {
        //luu vao db
        Notification notification = Notification.builder()
                .userId(targetUserId)
                .senderId(senderId)
                .type(NotificationType.FRIEND_ACCEPTED)
                .friendshipId(friendshipId)
                .isRead(false)
                .build();
        notification = notificationRepository.save(notification);

        //gui realtime qua websocket
        NotificationResponse response = mapToResponse(notification);
        messagingTemplate.convertAndSendToUser(
                targetUserId.toString(),
                "/queue/notifications",
                response
        );
    }

    @Override
    public List<NotificationResponse> getUnreadNotifications() {
        Long currentUserId = getCurrentUserId();
        List<Notification> notifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(currentUserId);
        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        Long currentUserId = getCurrentUserId();
        if (!notification.getUserId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public Long getUnreadCount() {
        Long currentUserId = getCurrentUserId();
        return notificationRepository.countByUserIdAndIsReadFalse(currentUserId);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaim("userId");
    }
    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = NotificationResponse.toNotification(notification);
        // Thêm thông tin sender
        userRepository.findById(notification.getSenderId()).ifPresent(sender -> {
            response.setSenderUsername(sender.getUsername());
            response.setSenderDisplayName(sender.getDisplayName());
            response.setSenderAvatarUrl(sender.getAvatarUrl());
        });
        return response;
    }
}
