package com.example.nationalpetition.controller.alarm;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.service.alarm.AlarmService;
import com.example.nationalpetition.service.alarm.dto.response.AlarmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AlarmController {

	private final AlarmService alarmService;

	@Auth
	@GetMapping("/api/v1/member/alarms")
	public ApiResponse<List<AlarmResponse>> getAlarms(@MemberId Long memberId) {
		return ApiResponse.success(alarmService.getAlarms(memberId));
	}

	@Auth
	@PutMapping("/api/v1/member/alarm/{alarmId}/check")
	public ApiResponse<AlarmResponse> checkAlarm(@PathVariable Long alarmId, @MemberId Long memberId) {
		return ApiResponse.success(alarmService.checkAlarm(alarmId, memberId));
	}

	@Auth
	@DeleteMapping("/api/v1/member/alarm/{alarmId}")
	public ApiResponse<String> deleteAlarm(@PathVariable Long alarmId, @MemberId Long memberId) {
		alarmService.deleteAlarm(alarmId, memberId);
		return ApiResponse.OK;
	}

}
