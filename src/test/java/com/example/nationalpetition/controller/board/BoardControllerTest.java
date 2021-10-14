package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.DeleteBoardLikeRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.BoardLikeCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@AutoConfigureRestDocs(uriHost = "3.36.186.100")
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

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    private Token token;

    @BeforeEach
    void setUpToken() {
        token = tokenService.generateToken(1L);
    }

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
    }

    @DisplayName("청원 크롤링 서버에서 가져와서 나의 제목과 컨텐츠와 함께 저장한다")
    @Test
    void 청원_게시글_생성한다() throws Exception {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www1.president.go.kr/petitions/1");

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
                                fieldWithPath("title").description("나의 청원 제목"),
                                fieldWithPath("content").description("나의 청원 글"),
                                fieldWithPath("petitionUrl").description("청원 url")
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
                                fieldWithPath("data.category").description("청원 카테고리"),
                                fieldWithPath("data.petitionCreatedAt").description("청원 시작일"),
                                fieldWithPath("data.petitionFinishedAt").description("청원 만료일"),
                                fieldWithPath("data.viewCounts").description("조회수"),
                                fieldWithPath("data.boardCommentCounts").description("댓글 개수"),
                                fieldWithPath("data.boardLikeCounts").description("좋아요 개수"),
                                fieldWithPath("data.boardUnLikeCounts").description("싫어요 개수"),
                                fieldWithPath("data.createdDate").description("생성날짜")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

//    @DisplayName("청원 크롤링 서버에서 가져와서 나의 제목과 컨텐츠와 함께 저장한다 하지만 petitionUrl이 틀릴경우 예외가 발생한다")
//    @Test
//    void 청원_게시글_생성하는데_petitionUrl_이_틀릴경우_예외발생() throws Exception {
//        // given
//        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www.president.go.kr/petitions/1");
//
//        // when & then
//        final ResultActions resultActions = mockMvc.perform(
//                        post("/api/v1/board")
//                                .header("Authorization", "Bearer ".concat(token.getToken()))
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andDo(document("board/createException",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
//                        requestHeaders(
//                                headerWithName("Authorization").description("토큰")
//                        ),
//                        requestFields(
//                                fieldWithPath("title").description("나의 청원 제목"),
//                                fieldWithPath("content").description("나의 청원 글"),
//                                fieldWithPath("petitionUrl").description("청원 url")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("code"),
//                                fieldWithPath("message").description("메세지"),
//                                fieldWithPath("data").description("")
//                        )
//                ));
//        resultActions.andExpect(status().is4xxClientError());
//    }

    @DisplayName("내가_작성한_게시글을_수정한다")
    @Test
    void 청원_게시글_수정한다() throws Exception {
        // given
        Board board = BoardCreator.create(1L, "title1", "content1");
        boardRepository.save(board);
        UpdateBoardRequest request = UpdateBoardRequest.testInstance(board.getId(), "updateTitle", "updateContent");

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
                                fieldWithPath("data.category").description("청원 카테고리"),
                                fieldWithPath("data.petitionCreatedAt").description("청원 시작일"),
                                fieldWithPath("data.petitionFinishedAt").description("청원 만료일"),
                                fieldWithPath("data.viewCounts").description("조회수"),
                                fieldWithPath("data.boardCommentCounts").description("댓글 개수"),
                                fieldWithPath("data.boardLikeCounts").description("좋아요 개수"),
                                fieldWithPath("data.boardUnLikeCounts").description("싫어요 개수"),
                                fieldWithPath("data.createdDate").description("생성날짜")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("게시글_아이디로_해당_게시글을_불러온다")
    @Test
    void 게시글을_불러온다() throws Exception {
        // given
        Board board = BoardCreator.create(1L, "title1", "content1");
        boardRepository.save(board);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/getBoard/{id}", board.getId())
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
                                fieldWithPath("data.category").description("청원 카테고리"),
                                fieldWithPath("data.petitionCreatedAt").description("청원 시작일"),
                                fieldWithPath("data.petitionFinishedAt").description("청원 만료일"),
                                fieldWithPath("data.viewCounts").description("조회수"),
                                fieldWithPath("data.boardCommentCounts").description("댓글 개수"),
                                fieldWithPath("data.boardLikeCounts").description("좋아요 개수"),
                                fieldWithPath("data.boardUnLikeCounts").description("싫어요 개수"),
                                fieldWithPath("data.createdDate").description("생성날짜")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글리스트를_불러온다() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=1&size=10")
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
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("작성한 유저"),
                                fieldWithPath("data.boardList[].petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data.boardList[].title").description("나의 작성한 제목"),
                                fieldWithPath("data.boardList[].petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data.boardList[].content").description("내가 작성한 글"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("청원 수"),
                                fieldWithPath("data.boardList[].category").description("청원 카테고리"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("청원 시작일"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("청원 만료일"),
                                fieldWithPath("data.boardList[].viewCounts").description("조회수"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("댓글 개수"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("좋아요 개수"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("싫어요 개수"),
                                fieldWithPath("data.boardList[].createdDate").description("생성날짜"),
                                fieldWithPath("data.boardCounts").description("게시글 총 개수")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글리스트를_불러온다2() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        board1.incrementViewCount();
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=1&size=10&sort=viewCounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/list2",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("타이틀 기준 검색어 [required] required 아니게 바꿀까??"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10"),
                                parameterWithName("sort").description("정렬할 필드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("작성한 유저"),
                                fieldWithPath("data.boardList[].petitionTitle").description("크롤링 해서 가져온 제목"),
                                fieldWithPath("data.boardList[].title").description("나의 작성한 제목"),
                                fieldWithPath("data.boardList[].petitionContent").description("크롤링해서 가져온 글"),
                                fieldWithPath("data.boardList[].content").description("내가 작성한 글"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("청원 수"),
                                fieldWithPath("data.boardList[].category").description("청원 카테고리"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("청원 시작일"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("청원 만료일"),
                                fieldWithPath("data.boardList[].viewCounts").description("조회수"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("댓글 개수"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("좋아요 개수"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("싫어요 개수"),
                                fieldWithPath("data.boardList[].createdDate").description("생성날짜"),
                                fieldWithPath("data.boardCounts").description("게시글 총 개수")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글_찬성_반대() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        Board board2 = BoardCreator.create(1L, "title2", "content2");
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

    @Test
    void 게시글_찬성_반대_삭제() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        boardRepository.save(board1);
        BoardLike boardLike = BoardLikeCreator.create(board1.getId(), 1L, BoardState.LIKE);
        boardLikeRepository.save(boardLike);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/board/like/{boardId}", board1.getId())
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/unlike",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("ok")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 게시글_찬성_반대_상태_보여주기() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        boardRepository.save(board1);
        BoardLike boardLike = BoardLikeCreator.create(board1.getId(), 1L, BoardState.LIKE);
        boardLikeRepository.save(boardLike);

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/board/status/{boardId}", board1.getId())
                                .header("Authorization", "Bearer ".concat(token.getToken()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/like/state",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("토큰")
                        ),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("찬성했는지 안했는지 상태 만약 없으면 null")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

}

