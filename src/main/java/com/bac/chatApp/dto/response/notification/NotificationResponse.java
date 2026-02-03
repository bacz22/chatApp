package com.bac.chatApp.dto.response.notification;

import com.bac.chatApp.model.Notification;
import com.bac.chatApp.model.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long senderId;
    private NotificationType type;
    private Long friendShipId;
    private Boolean isRead;
    private LocalDateTime createdAt;

    private String senderUsername;
    private String senderDisplayName;
    private String senderAvatarUrl;

    public static NotificationResponse toNotification(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .senderId(notification.getSenderId())
                .type(notification.getType())
                .friendShipId(notification.getFriendshipId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
