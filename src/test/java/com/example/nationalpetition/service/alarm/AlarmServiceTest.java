package com.example.nationalpetition.service.alarm;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmRepository;
import com.example.nationalpetition.domain.alarm.AlarmState;
import com.example.nationalpetition.service.alarm.dto.request.CommentCreatedAlarmMessage;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static com.example.nationalpetition.domain.alarm.AlarmEventType.COMMENT_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AlarmServiceTest {

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private AlarmRepository alarmRepository;

	@AfterEach
	void cleanUp() {
		alarmRepository.deleteAll();
	}

	@Test
	void 새로운_알림을_대상자들에게_추가한다() {
		// given
		String nickName = "댓글 작성자 닉네임";
		String boardTitle = "게시글 제목";
		Long boardId = 100L;

		String message = String.format(COMMENT_CREATED.getMessageFormat(), nickName, boardTitle);
		Set<Long> memberIds = Set.of(1L, 2L);

		// when
		alarmService.sendAlarm(memberIds, CommentCreatedAlarmMessage.of(boardId, boardTitle, nickName));

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).hasSize(2);
		assertThat(alarms).extracting(Alarm::getMemberId).containsExactlyInAnyOrderElementsOf(memberIds);
		assertThat(alarms).extracting(Alarm::getState).containsOnly(AlarmState.UNCHECKED);
		assertThat(alarms).extracting(Alarm::getEventType).containsOnly(COMMENT_CREATED);
		assertThat(alarms).extracting(Alarm::getMessage).containsOnly(message);
		assertThat(alarms).extracting(Alarm::getBoardId).containsOnly(boardId);
	}

	@Test
	void 알람을_읽음_표시하면_State가_CHECKED로_변경된다() {
		// given
		Long memberId = 1000L;

		Alarm alarm = Alarm.newInstance(memberId, COMMENT_CREATED, "알람", 10L);
		alarmRepository.save(alarm);

		// when
		alarmService.checkAlarm(alarm.getId(), memberId);

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).hasSize(1);
		assertThat(alarms.get(0).getState()).isEqualTo(AlarmState.CHECKED);
	}

	@Test
	void 이미_삭제된_알림을_표시요청시_NOTFOUND() {
		// given
		Long memberId = 1000L;

		Alarm alarm = Alarm.newInstance(memberId, COMMENT_CREATED, "알람", 10L);
		alarm.delete();
		alarmRepository.save(alarm);

		// when
		assertThatThrownBy(() -> alarmService.checkAlarm(alarm.getId(), memberId)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void 알람을_삭제한다() {
		// given
		Long memberId = 1000L;

		Alarm alarm = Alarm.newInstance(memberId, COMMENT_CREATED, "알람", 10L);
		alarmRepository.save(alarm);

		// when
		alarmService.deleteAlarm(alarm.getId(), memberId);

		// then
		List<Alarm> alarms = alarmRepository.findAll();
		assertThat(alarms).hasSize(1);
		assertThat(alarms.get(0).getState()).isEqualTo(AlarmState.DELETED);
	}

	@Test
	void 다른_사용자의_알람을_삭제할_수없다() {
		// given
		Alarm alarm = Alarm.newInstance(10000L, COMMENT_CREATED, "알람", 10L);
		alarmRepository.save(alarm);

		// when
		assertThatThrownBy(() -> alarmService.checkAlarm(alarm.getId(), -1L)).isInstanceOf(NotFoundException.class);
	}

}