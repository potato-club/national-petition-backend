package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "3.36.229.76")
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
    }

    @DisplayName("청원 크롤링 서버에서 가져와서 나의 제목과 컨텐츠와 함께 저장한다")
    @Test
    void 청원_게시글_생성한다() throws Exception {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance(1L, "title", "content");

        Token token = tokenService.generateToken(1L);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board")
                        .header("Authorization", "Bearer ".concat(token.getToken()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        requestFields(
                                fieldWithPath("petitionId").description("청원글 아이디"),
                                fieldWithPath("title").description("나의 청원 제목"),
                                fieldWithPath("content").description("나의 청원 글")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("작성한 유저"),
                                fieldWithPath("data.petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data.title").description("나의 작성한 제목"),
                                fieldWithPath("data.petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data.content").description("내가 작성한 글"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("청원 수"),
                                fieldWithPath("data.category").description("청원 카테고리")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("내가_작성한_게시글을_수정한다")
    @Test
    void 청원_게시글_수정한다() throws Exception {
        // given
        Board board = new Board(1L, "petitionTitle", "title", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.save(board);
        UpdateBoardRequest request = UpdateBoardRequest.testInstance(board.getId(), "updateTitle", "updateContent");

        Token token = tokenService.generateToken(1L);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/update")
                        .header("Authorization", "Bearer ".concat(token.getToken()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        requestFields(
                                fieldWithPath("boardId").description("게시글 아이디"),
                                fieldWithPath("title").description("나의 청원 제목"),
                                fieldWithPath("content").description("나의 청원 글")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("작성한 유저"),
                                fieldWithPath("data.petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data.title").description("나의 작성한 제목"),
                                fieldWithPath("data.petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data.content").description("내가 작성한 글"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("청원 수"),
                                fieldWithPath("data.category").description("청원 카테고리")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("게시글_아이디로_해당_게시글을_불러온다")
    @Test
    void 게시글을_불러온다() throws Exception {
        // given
        Token token = tokenService.generateToken(1L);

        Board board = new Board(1L, "petitionTitle", "title", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.save(board);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/board/{id}", board.getId())
                        .header("Authorization", "Bearer ".concat(token.getToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                            parameterWithName("id").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("작성한 유저"),
                                fieldWithPath("data.petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data.title").description("나의 작성한 제목"),
                                fieldWithPath("data.petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data.content").description("내가 작성한 글"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("청원 수"),
                                fieldWithPath("data.category").description("청원 카테고리")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글리스트를_불러온다() throws Exception {
        // given
        Token token = tokenService.generateToken(1L);

        Board board1 = new Board(1L, "petitionTitle", "title1", "petitionContent", "content", "url", "10000", "사회문제");
        Board board2 = new Board(1L, "petitionTitle", "title2", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/board/list?search=&page=0&size=10")
                        .header("Authorization", "Bearer ".concat(token.getToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("타이틀 기준 검색어 [required] required 아니게 바꿀까??"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data[].boardId").description("boardId"),
                                fieldWithPath("data[].memberId").description("작성한 유저"),
                                fieldWithPath("data[].petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data[].title").description("나의 작성한 제목"),
                                fieldWithPath("data[].petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data[].content").description("내가 작성한 글"),
                                fieldWithPath("data[].petitionUrl").description("url"),
                                fieldWithPath("data[].petitionsCount").description("청원 수"),
                                fieldWithPath("data[].category").description("청원 카테고리")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글_찬성_반대() throws Exception {
        // given
        Token token = tokenService.generateToken(1L);

        Board board1 = new Board(1L, "petitionTitle", "title1", "petitionContent", "content", "url", "10000", "사회문제");
        Board board2 = new Board(1L, "petitionTitle", "title2", "petitionContent", "content", "url", "10000", "사회문제");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        BoardLikeRequest request = BoardLikeRequest.testInstance(board1.getId(), BoardState.LIKE);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/board/like")
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/like",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        requestFields(
                                fieldWithPath("boardId").description("게시글 아이디"),
                                fieldWithPath("boardState").description("게시글 찬성/반대")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("ok")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

}

