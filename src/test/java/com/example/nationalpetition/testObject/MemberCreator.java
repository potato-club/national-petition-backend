package com.example.nationalpetition.testObject;

import com.example.nationalpetition.domain.member.entity.Member;

public class MemberCreator {

    public static Member create() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("name")
                .picture("http://naver.com")
                .build();
        member.addNickName("nickname");
        return member;
    }

    public static Member create(String email, String name) {
        Member member = Member.builder()
                .email(email)
                .name("name")
                .picture("http://naver.com")
                .build();
        member.addNickName(name);
        return member;
    }

}
