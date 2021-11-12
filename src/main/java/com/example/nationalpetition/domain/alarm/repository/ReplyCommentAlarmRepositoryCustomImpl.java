package com.example.nationalpetition.domain.alarm.repository;

import com.example.nationalpetition.domain.alarm.entity.QReplyCommentAlarm;
import com.example.nationalpetition.domain.alarm.entity.ReplyCommentAlarm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.nationalpetition.domain.alarm.entity.QCommentAlarm.commentAlarm;
import static com.example.nationalpetition.domain.alarm.entity.QReplyCommentAlarm.*;

@RequiredArgsConstructor
public class ReplyCommentAlarmRepositoryCustomImpl implements ReplyCommentAlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReplyCommentAlarm>  findByMemberIdAndIsReadIsFalseAndOrderByIdDesc(Long memberId) {
        return queryFactory
                .selectFrom(replyCommentAlarm)
                .where(
                        replyCommentAlarm.memberId.eq(memberId),
                        replyCommentAlarm.isRead.isFalse()
                )
                .orderBy(replyCommentAlarm.id.desc())
                .fetch();
    }
}
