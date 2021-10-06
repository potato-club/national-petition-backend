package com.example.nationalpetition.service.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberResponse findById(Long memberId);

    Member findByEmail(String email);

    String addNickName(Long memberId, NickNameRequest request);

    String deleteMember(Long memberId);

    List<BoardInfoResponseInMyPage> getMyBoardList(Long memberId, Pageable pageable);
}
