package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.exception.JwtTokenException;
import com.example.nationalpetition.exception.NotFoundException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "3.36.229.76")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("회원 정보 조회 (마이페이지 기준)")
    void getMyInfo() throws Exception {
        //given
        final Long memberId = MemberServiceUtils.saveMember(memberRepository);
        final Token token = tokenService.generateToken(memberId);
        //when
        final ResultActions resultActions = mockMvc
                .perform(
                        get("/api/v1/mypage/info")
                        .header("Authorization", token.getToken()))
                .andDo(print())
                .andDo(document("mypage/info",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.picture").type(JsonFieldType.STRING).description("프로필사진"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("닉네임")
                            )
                        )
                );
        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 정보 조회 (토큰 값 틀렸을 경우) -> 아이디 찾지 못함")
    void getMyInfo2() throws Exception {
        //given
        final Long memberId = MemberServiceUtils.saveMember(memberRepository);
        final Token token = tokenService.generateToken(memberId+1L);
        //when then
        final MvcResult mockResult = mockMvc
                .perform(
                        get("/api/v1/mypage/info")
                                .header("Authorization", token.getToken()))
                .andDo(print())
                .andDo(document("mypage/info/notFound"))
                .andExpect(result -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(NotFoundException.class)))
                .andReturn();

    }

}