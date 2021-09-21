package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.NickNameRequest;

import java.util.Optional;

public interface MemberService {
    Member findById(Long memberId);

    Member findByEmail(String email);

    Member addNickName(Long memberId, NickNameRequest request);
}
