package com.bac.chatApp.controller;

import com.bac.chatApp.dto.response.ApiResponse;
import com.bac.chatApp.dto.response.notification.NotificationResponse;
import com.bac.chatApp.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService iNotificationService;

    @GetMapping("")
    public ApiResponse<List<NotificationResponse>> getUnreadNotifications(){
        return ApiResponse.<List<NotificationResponse>>builder()
                .errorCode(0)
                .message("")
                .data(iNotificationService.getUnreadNotifications())
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Long> getUnreadCount(){
        return ApiResponse.<Long>builder()
                .errorCode(0)
                .message("")
                .data(iNotificationService.getUnreadCount())
                .build();
    }

    @PostMapping("/{notificationId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long notificationId){
        iNotificationService.markAsRead(notificationId);
        return ApiResponse.<Void>builder()
                .errorCode(0)
                .message("Đã đánh dấu đã đọc")
                .build();
    }
}
