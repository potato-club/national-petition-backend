package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.MemberPageRequest;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.dto.member.response.MyPageBoardListResponse;
import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;

import java.util.List;

public interface MemberService {
    MemberResponse findById(Long memberId);

    Member findByEmail(String email);

    String addNickName(Long memberId, NickNameRequest request);

    String deleteMember(Long memberId);

    MyPageBoardListResponse getMyBoardList(Long memberId, MemberPageRequest request);

    List<NotificationInfoResponse> retrieveNotification(Long memberId);
}
