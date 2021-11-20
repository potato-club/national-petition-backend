package com.example.nationalpetition.controller.notification;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.notification.request.UpdateBoardNotificationRequest;
import com.example.nationalpetition.dto.notification.request.UpdateMemberNotificationRequest;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.example.nationalpetition.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 리스트를 받는 api", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/api/v1/notification/list")
    public ApiResponse<List<NotificationInfoResponse>> retrieveNotification(@MemberId Long memberId) {
        return ApiResponse.success(notificationService.retrieveNotification(memberId));
    }

    @Operation(summary = "알림을 읽었다는 표시로 하는 api", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/notification/state/{notificationId}")
    public ApiResponse<String> notificationIsRead(@PathVariable Long notificationId, @MemberId Long memberId) {
        notificationService.notificationIsRead(notificationId, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "마이페이지에서 나의 알림 상태를 변경하는 api", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/member/notification/state")
    public ApiResponse<String> updateMemberNotification(@RequestBody @Valid UpdateMemberNotificationRequest request, @MemberId Long memberId) {
        notificationService.updateMemberNotification(request.getState(), memberId);
        return ApiResponse.OK;
    }

}
