package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.nationalpetition.controller.ApiDocumentUtils.getDocumentRequest;
import static com.example.nationalpetition.controller.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("청원 크롤링 서버에서 가져와서 나의 제목과 컨텐츠와 함께 저장한다")
    @Test
    void 청원_게시글_생성한다() throws Exception {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance(600403L, "title", "content", 1L);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("petitionId").description("청원글 아이디"),
                                fieldWithPath("title").description("나의 청원 제목"),
                                fieldWithPath("content").description("나의 청원 글"),
                                fieldWithPath("memberId").description("memberId")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("boardId"),
                                fieldWithPath("data.petitionTitle").description("boardId"),
                                fieldWithPath("data.title").description("boardId"),
                                fieldWithPath("data.petitionContent").description("boardId"),
                                fieldWithPath("data.content").description("boardId"),
                                fieldWithPath("data.petitionUrl").description("boardId"),
                                fieldWithPath("data.petitionsCount").description("boardId"),
                                fieldWithPath("data.category").description("boardId")
                        )
                ));

        resultActions.andExpect(status().isOk());

    }

}
