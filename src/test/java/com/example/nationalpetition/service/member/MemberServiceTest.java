package com.example.nationalpetition.service.member;

import com.example.nationalpetition.controller.member.MemberServiceUtils;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 정보 조회 (마이페이지)")
    void getMyInfo()  {
        //given
        final Long memberId = MemberServiceUtils.saveMember(memberRepository);
        //when
        final MemberResponse memberResponse = memberService.findById(memberId);
        //then
        assertThat(memberResponse.getName()).isEqualTo("이름");
        assertThat(memberResponse.getEmail()).isEqualTo("aa@naver.com");
    }

    @Test
    @DisplayName("회원 정보 조회 중 리포지토리에 저장되지 않은 Id값으로 회원을 찾는 경우 예외 발생")
    void getMyInfo_fail() {
        //given
        Long memberId = 123123L;
        //when && then
        assertThatThrownBy( () ->memberService.findById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_EXCEPTION_USER.getMessage());
    }
}
