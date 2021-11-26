package com.example.nationalpetition.service.alarm.dto.request;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreatedAlarmMessage implements AlarmMessage {

	private final Long boardId;
	private final String boardTitle;
	private final String nickName;

	public static CommentCreatedAlarmMessage of(Long boardId, String boardTitle, String nickName) {
		return new CommentCreatedAlarmMessage(boardId, boardTitle, nickName);
	}

	@Override
	public Alarm toEntity(Long memberId) {
		return Alarm.newInstance(memberId, AlarmEventType.COMMENT_CREATED, String.format(AlarmEventType.COMMENT_CREATED.getMessageFormat(), nickName, boardTitle), boardId);
	}

}
