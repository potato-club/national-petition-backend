package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;

public interface MemberService {
    Member findByUid(String uid);
}
