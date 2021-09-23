package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;

public class MemberServiceUtils {

    public static Long saveMember(MemberRepository memberRepository) {
        final Member member = Member.of("이름", "aa@naver.com", "picturepicture");
        member.addNickName("닉네임");
        return memberRepository.save(member).getId();
    }
}
