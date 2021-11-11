package com.example.nationalpetition.domain.alarm;

import com.example.nationalpetition.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Alarm extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AlarmEventType eventType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AlarmState state;

	@Column(nullable = false)
	private String message;

	private Long boardId;

	private Alarm(Long memberId, AlarmEventType eventType, AlarmState state, String message, Long boardId) {
		this.memberId = memberId;
		this.eventType = eventType;
		this.state = state;
		this.message = message;
		this.boardId = boardId;
	}

	public static Alarm newInstance(Long memberId, AlarmEventType eventType, String message, Long boardId) {
		return new Alarm(memberId, eventType, AlarmState.UNCHECKED, message, boardId);
	}

	public void check() {
		this.state = AlarmState.CHECKED;
	}

	public void delete() {
		this.state = AlarmState.DELETED;
	}

}
