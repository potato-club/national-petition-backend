package com.example.nationalpetition.service.member;

import com.example.nationalpetition.controller.member.MemberServiceUtils;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.DeleteMemberRepository;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.member.DeleteMessageConst;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.DeleteMemberResponse;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.utils.ValidationUtils;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.AlreadyExistException;
import com.example.nationalpetition.utils.error.exception.DuplicateException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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

    @Test
    @DisplayName("회원 닉네임 등록 성공")
    void addNickName() {
        //given
        final Member member = memberRepository.save(Member.of("이름", "email@email.com", "picture"));
        final NickNameRequest request = new NickNameRequest("닉네임");
        //when
        final MemberResponse memberResponse = memberService.addNickName(member.getId(), request);
        //then
        assertThat(memberResponse.getNickName()).isEqualTo("닉네임");
        assertThat(memberResponse.getName()).isEqualTo("이름");
        assertThat(memberResponse.getEmail()).isEqualTo("email@email.com");
        assertThat(memberResponse.getPicture()).isEqualTo("picture");
    }

    @Test
    @DisplayName("닉네임 등록 실패 --> 중복되는 닉네임")
    void addNickName_fail1() {
        //given
        MemberServiceUtils.saveMember(memberRepository);
        final Member member = memberRepository.save(Member.of("아아아", "eee@ee.ee", "piiicture"));
        final NickNameRequest request = new NickNameRequest("닉네임");
        //when && then
        assertThatThrownBy(() -> memberService.addNickName(member.getId(), request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorCode.DUPLICATE_EXCEPTION_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("닉네임 등록 실패 --> 이미 닉네임을 등록한 계정")
    void addNickName_fail2() {
        //given
        final Long memberId = MemberServiceUtils.saveMember(memberRepository);
        final NickNameRequest request = new NickNameRequest("닉네임222");
        //when && then
        assertThatThrownBy(() -> memberService.addNickName(memberId, request))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(ErrorCode.ALREADY_EXIST_EXCEPTION_ADD_NICKNAME.getMessage());

    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteMember() {
        //given
        final Long memberId = MemberServiceUtils.saveMember(memberRepository);
        final MemberResponse member = memberService.findById(memberId);
        //when
        final DeleteMemberResponse result = memberService.deleteMember(memberId);
        //then
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getMessage()).isEqualTo(DeleteMessageConst.MESSAGE);
        assertThatThrownBy(() -> memberService.findById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_EXCEPTION_USER.getMessage());

    }
}
