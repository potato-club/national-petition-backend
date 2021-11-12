package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.CommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.QCommentAlarm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.nationalpetition.domain.alarm.entity.QCommentAlarm.*;

@RequiredArgsConstructor
public class CommentAlarmRepositoryCustomImpl implements CommentAlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<CommentAlarm>  findByMemberIdAndIsReadIsFalseAndOrderByIdDesc(Long memberId) {
        return queryFactory
                        .selectFrom(commentAlarm)
                        .where(
                                commentAlarm.memberId.eq(memberId),
                                commentAlarm.isRead.isFalse()
                        )
                        .orderBy(commentAlarm.id.desc())
                        .fetch();
    }
}
