package com.example.nationalpetition.controller.notification;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.domain.notification.Notification;
import com.example.nationalpetition.domain.notification.NotificationTemplate;
import com.example.nationalpetition.domain.notification.repository.NotificationRepository;
import com.example.nationalpetition.dto.notification.request.UpdateMemberNotificationRequest;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.testObject.MemberCreator;
import com.example.nationalpetition.testObject.NotificationCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.example.nationalpetition.controller.ApiDocumentUtils.getDocumentRequest;
import static com.example.nationalpetition.controller.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriHost = "3.36.186.100")
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenService tokenService;

    @AfterEach
    void clean() {
        notificationRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("알람 리스트를 가져온다")
    @Test
    void 알람_리스트를_가져온다() throws Exception {
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Token token = tokenService.generateToken(member.getId());
        // given
        Notification notification1 = NotificationCreator.create(member.getId(), 1L, NotificationTemplate.CREATE_COMMENT, "헬로우1");
        Notification notification2 = NotificationCreator.create(member.getId(), 3L, NotificationTemplate.CREATE_COMMENT, "헬로우2");
        Notification notification3 = NotificationCreator.create(member.getId(), 2L, NotificationTemplate.CREATE_COMMENT, "헬로우3");
        notificationRepository.saveAll(Arrays.asList(notification1, notification2, notification3));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/notification/list")
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("notification/list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data[].id").description("notificationId"),
                                fieldWithPath("data[].content").description("notification content"),
                                fieldWithPath("data[].isRead").description("읽었는지 상태 표시")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("알람 읽었다는 표시로 변경한다")
    @Test
    void 알람_상태_변경() throws Exception {
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Token token = tokenService.generateToken(member.getId());
        // given
        Notification notification1 = NotificationCreator.create(member.getId(), 1L, NotificationTemplate.CREATE_COMMENT, "헬로우1");
        notificationRepository.save(notification1);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/notification/state/" + notification1.getId())
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("notification/state",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("OK")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("멤버의 전체 알람 on, off")
    @Test
    void 전체_알람_온오프() throws Exception {
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Token token = tokenService.generateToken(member.getId());
        // given
        Notification notification1 = NotificationCreator.create(member.getId(), 1L, NotificationTemplate.CREATE_COMMENT, "헬로우1");
        notificationRepository.save(notification1);

        UpdateMemberNotificationRequest request = new UpdateMemberNotificationRequest(true);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/member/notification/state")
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("notification/state",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("OK")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

}
