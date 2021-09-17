package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    Member findById(Long memberId);
}
