package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;

public interface MemberService {
    MemberResponse findById(Long memberId);

    Member findByEmail(String email);

    MemberResponse addNickName(Long memberId, NickNameRequest request);
}
