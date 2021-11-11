package com.example.nationalpetition.controller.alarm;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;
import com.example.nationalpetition.service.alarm.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

//    @Operation(summary = "알람 등록 후 게시글에 댓글 또는 댓글에 대댓글이 달렸을 경우 보관하는 API", security = {@SecurityRequirement(name = "BearerKey")})
//    @Auth
//    @GetMapping("/api/v1/alarm/list")
//    public ApiResponse<AlarmListResponse> getAlarmList(Long memberId) {
//
//        alarmService.getAlarmList(memberId);
//    }
}
