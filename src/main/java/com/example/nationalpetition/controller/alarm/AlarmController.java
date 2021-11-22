package com.example.nationalpetition.controller.alarm;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.alarm.AlarmListResponse;
import com.example.nationalpetition.dto.alarm.AlarmResponse;
import com.example.nationalpetition.service.alarm.AlarmReader;
import com.example.nationalpetition.service.alarm.AlarmStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmStore alarmStore;
    private final AlarmReader alarmReader;

    @Operation(summary = "알람들을 가져오는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/api/v1/alarm/list")
    public ApiResponse<AlarmListResponse> getAlarmList(Long memberId) {
        return ApiResponse.success(alarmReader.getAlarmList(memberId));
    }

    @Operation(summary = "알람 확인 시 알람 상태를 읽음으로 바꾸는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/api/v1/member/alarm/{alarmId}/check")
    public ApiResponse<AlarmResponse> checkAlarm(@PathVariable Long alarmId, @MemberId Long memberId) {
        return ApiResponse.success(alarmStore.checkAlarm(alarmId, memberId));
    }

    @Operation(summary = "알람을 삭제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v1/member/alarm/{alarmId}")
    public ApiResponse<String> deleteAlarm(@PathVariable Long alarmId, @MemberId Long memberId) {
        alarmStore.deleteAlarm(alarmId, memberId);
        return ApiResponse.OK;
    }
}
