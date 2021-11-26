package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.Alarm;
import com.example.nationalpetition.domain.alarm.AlarmState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.nationalpetition.domain.alarm.QAlarm.alarm;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Alarm> findNotDeletedAlarmsByMemberId(Long memberId) {
		return queryFactory.selectFrom(alarm)
				.where(
						alarm.memberId.eq(memberId),
						alarm.state.in(AlarmState.CHECKED, AlarmState.UNCHECKED)
				)
				.orderBy(alarm.id.desc())
				.fetch();
	}

	@Override
	public Alarm findByIdAndMemberId(Long alarmId, Long memberId) {
		return queryFactory.selectFrom(alarm)
				.where(
						alarm.id.eq(alarmId),
						alarm.memberId.eq(memberId),
						alarm.state.in(AlarmState.CHECKED, AlarmState.UNCHECKED)
				)
				.fetchOne();
	}

}
