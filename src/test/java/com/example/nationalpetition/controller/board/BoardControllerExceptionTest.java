package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.security.jwt.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriHost = "3.36.186.100")
@AutoConfigureMockMvc
public class BoardControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
    }

    @Test
    void 게시글리스트를_불러온다_예외() throws Exception {
        // given
        Board board1 = new Board(1L, "petitionTitle", "title1", "petitionContent", "content", "url", "10000", "사회문제");
        Board board2 = new Board(1L, "petitionTitle", "title2", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/exception/size",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("타이틀 기준 검색어 [required] required 아니게 바꿀까??"),
                                parameterWithName("page").description("page [required]")
                        ),
                        responseFields(
                                fieldWithPath("errors[].message").description("code")
                        )
                ));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    void 게시글리스트를_불러온다_예외2() throws Exception {
        // given
        Board board1 = new Board(1L, "petitionTitle", "title1", "petitionContent", "content", "url", "10000", "사회문제");
        Board board2 = new Board(1L, "petitionTitle", "title2", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?&page=0&size=2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/exception/search",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size")
                        ),
                        responseFields(
                                fieldWithPath("errors[].message").description("code")
                        )
                ));
        resultActions.andExpect(status().is4xxClientError());
    }

}
