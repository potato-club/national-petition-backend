package com.example.nationalpetition.service.alarm.dto.request;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyCommentCreatedAlarmMessage implements AlarmMessage {

	private final Long boardId;
	private final String boardTitle;
	private final String nickName;

	public static ReplyCommentCreatedAlarmMessage of(Long boardId, String boardTitle, String nickName) {
		return new ReplyCommentCreatedAlarmMessage(boardId, boardTitle, nickName);
	}

	@Override
	public Alarm toEntity(Long memberId) {
		AlarmEventType eventType = AlarmEventType.RE_COMMENT_CREATED;
		return Alarm.newInstance(memberId, eventType, String.format(eventType.getMessageFormat(), nickName, boardTitle), boardId);
	}

}
