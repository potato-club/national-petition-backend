package com.example.nationalpetition.controller.notification;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Auth
    @GetMapping("/api/v1/notification/list")
    public ApiResponse<List<NotificationInfoResponse>> retrieveNotification(@MemberId Long memberId) {
        notificationService.retrieveNotification(memberId);
        return ApiResponse.success(notificationService.retrieveNotification(memberId));
    }

    @Auth
    @PostMapping("/api/v1/notification/state/{notificationId}")
    public ApiResponse<String> notificationIsRead(@PathVariable Long notificationId, @MemberId Long memberId) {
        notificationService.notificationIsRead(notificationId, memberId);
        return ApiResponse.OK;
    }

}
