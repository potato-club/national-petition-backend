package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.AlarmRequest;
import com.example.nationalpetition.dto.member.request.MemberPageRequest;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.dto.member.response.MyPageBoardListResponse;

public interface MemberService {
    MemberResponse findById(Long memberId);

    Member findByEmail(String email);

    String addNickName(Long memberId, NickNameRequest request);

    String deleteMember(Long memberId);

    MyPageBoardListResponse getMyBoardList(Long memberId, MemberPageRequest request);

    void changeBoardAlarm(Long memberId, AlarmRequest request);

    void changeCommentAlarm(Long memberId, AlarmRequest request);
}
