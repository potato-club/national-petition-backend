package com.example.nationalpetition.controller.alarm;

import com.example.nationalpetition.event.CommentCreatedEvent;
import com.example.nationalpetition.service.alarm.EventAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AlarmEventListener {

	private final EventAlarmService eventAlarmService;

	@Async
	@EventListener
	public void notifyCreatedComment(CommentCreatedEvent event) {
		eventAlarmService.notifyCreatedComment(event.getCommentId());
	}

}
