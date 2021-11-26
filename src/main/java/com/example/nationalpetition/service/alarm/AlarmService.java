package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmRepository;
import com.example.nationalpetition.service.alarm.dto.request.AlarmMessage;
import com.example.nationalpetition.service.alarm.dto.response.AlarmResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AlarmService {

	private final AlarmRepository alarmRepository;

	@Transactional
	public void sendAlarm(Set<Long> memberIds, AlarmMessage messageDto) {
		List<Alarm> alarms = memberIds.stream()
				.map(messageDto::toEntity)
				.collect(Collectors.toList());
		alarmRepository.saveAll(alarms);
	}

	@Transactional(readOnly = true)
	public List<AlarmResponse> getAlarms(Long memberId) {
		return alarmRepository.findNotDeletedAlarmsByMemberId(memberId).stream()
				.map(AlarmResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public AlarmResponse checkAlarm(Long alarmId, Long memberId) {
		Alarm alarm = findAlarmByIdAndMemberId(alarmId, memberId);
		alarm.check();
		return AlarmResponse.of(alarm);
	}

	@Transactional
	public void deleteAlarm(Long alarmId, Long memberId) {
		Alarm alarm = findAlarmByIdAndMemberId(alarmId, memberId);
		alarm.delete();
	}

	private Alarm findAlarmByIdAndMemberId(Long alarmId, Long memberId) {
		Alarm alarm = alarmRepository.findByIdAndMemberId(alarmId, memberId);
		if (alarm == null) {
			throw new NotFoundException(String.format("멤버 (%s)에게 해당하는 알람 (%s)은 존재하지 않습니다", memberId, alarmId), ErrorCode.NOT_FOUND_EXCEPTION_COMMENT);
		}
		return alarm;
	}

}
