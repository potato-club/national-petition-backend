package com.example.nationalpetition.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreatedEvent {

	private final Long commentId;

	public static CommentCreatedEvent of(Long commentId) {
		return new CommentCreatedEvent(commentId);
	}

}
