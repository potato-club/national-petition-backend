package com.example.nationalpetition.domain.member.repository;

import com.example.nationalpetition.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.nationalpetition.domain.member.entity.QMember.*;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean duplicateNickName(String nickName) {
        return queryFactory
                .selectOne()
                .from(member)
                .where(member.nickName.eq(nickName))
                .fetchFirst() != null;
    }
}
