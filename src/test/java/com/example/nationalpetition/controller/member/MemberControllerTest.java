package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
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
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "3.36.64.148")
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    EntityManager em;

    @Autowired
    TokenService tokenService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        final Member member = Member.of("이름", "aa@naver.com", "picturepicture");
        member.addNickName("닉네임");
        memberRepository.save(member);
    }

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("회원 정보 조회 (마이페이지 기준)")
    void getMyInfo() throws Exception {
        //given
        final Long memberId = 1L;
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

}