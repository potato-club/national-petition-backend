package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.Alarm;
import com.example.nationalpetition.domain.alarm.entity.AlarmState;
import com.example.nationalpetition.domain.alarm.entity.QAlarm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.nationalpetition.domain.alarm.entity.AlarmState.*;
import static com.example.nationalpetition.domain.alarm.entity.QAlarm.*;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findAlarmsByMemberIdWithNotDeleted(Long memberId) {
        return queryFactory.
                selectFrom(alarm)
                .where(alarm.state.in(CHECKED, UNCHECKED),
                        alarm.memberId.eq(memberId))
                .orderBy(alarm.memberId.desc())
                .fetch();
    }
}
