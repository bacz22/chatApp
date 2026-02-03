package com.bac.chatApp.service;

import com.bac.chatApp.dto.response.notification.NotificationResponse;

import java.util.List;

public interface INotificationService {

    void sendFriendRequestNotification(Long targetUserId, Long senderId, Long friendshipId);

    void sendFriendAcceptedNotification(Long targetUserId, Long senderId, Long friendshipId);

    List<NotificationResponse> getUnreadNotifications();

    void markAsRead(Long notificationId);

    Long getUnreadCount();
}
