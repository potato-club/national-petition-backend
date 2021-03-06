package com.example.nationalpetition.controller.board;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.BoardCategory;
import com.example.nationalpetition.domain.board.BoardLike;
import com.example.nationalpetition.domain.board.BoardState;
import com.example.nationalpetition.domain.board.repository.BoardLikeRepository;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.board.request.BoardLikeRequest;
import com.example.nationalpetition.dto.board.request.CreateBoardRequest;
import com.example.nationalpetition.dto.board.request.UpdateBoardRequest;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.testObject.BoardLikeCreator;
import com.example.nationalpetition.testObject.MemberCreator;
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

    @Autowired
    private MemberRepository memberRepository;

    private Token token;

    @BeforeEach
    void setUpToken() {
        token = tokenService.generateToken(1L);
    }

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
        boardLikeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("?????? ????????? ???????????? ???????????? ?????? ????????? ???????????? ?????? ????????????")
    @Test
    void ??????_?????????_????????????() throws Exception {
        // given
        CreateBoardRequest request = CreateBoardRequest.testInstance("title", "content", "www1.president.go.kr/petitions/100");

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
                                headerWithName("Authorization").description("??????")
                        ),
                        requestFields(
                                fieldWithPath("title").description("?????? ?????? ??????"),
                                fieldWithPath("content").description("?????? ?????? ???"),
                                fieldWithPath("petitionUrl").description("?????? url")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("????????? ??????"),
                                fieldWithPath("data.petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.title").description("?????? ????????? ??????"),
                                fieldWithPath("data.petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.content").description("?????? ????????? ???"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("?????? ???"),
                                fieldWithPath("data.category").description("?????? ????????????"),
                                fieldWithPath("data.petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.viewCounts").description("?????????"),
                                fieldWithPath("data.boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.createdDate").description("????????????"),
                                fieldWithPath("data.memberResponse").description("????????? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

//    @DisplayName("?????? ????????? ???????????? ???????????? ?????? ????????? ???????????? ?????? ???????????? ????????? petitionUrl??? ???????????? ????????? ????????????")
//    @Test
//    void ??????_?????????_???????????????_petitionUrl_???_????????????_????????????() throws Exception {
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
//                                headerWithName("Authorization").description("??????")
//                        ),
//                        requestFields(
//                                fieldWithPath("title").description("?????? ?????? ??????"),
//                                fieldWithPath("content").description("?????? ?????? ???"),
//                                fieldWithPath("petitionUrl").description("?????? url")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("code"),
//                                fieldWithPath("message").description("?????????"),
//                                fieldWithPath("data").description("")
//                        )
//                ));
//        resultActions.andExpect(status().is4xxClientError());
//    }

    @DisplayName("??????_?????????_????????????_????????????")
    @Test
    void ??????_?????????_????????????() throws Exception {
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
                                headerWithName("Authorization").description("??????")
                        ),
                        requestFields(
                                fieldWithPath("boardId").description("????????? ?????????"),
                                fieldWithPath("title").description("?????? ?????? ??????"),
                                fieldWithPath("content").description("?????? ?????? ???")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("????????? ??????"),
                                fieldWithPath("data.petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.title").description("?????? ????????? ??????"),
                                fieldWithPath("data.petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.content").description("?????? ????????? ???"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("?????? ???"),
                                fieldWithPath("data.category").description("?????? ????????????"),
                                fieldWithPath("data.petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.viewCounts").description("?????????"),
                                fieldWithPath("data.boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.createdDate").description("????????????"),
                                fieldWithPath("data.memberResponse").description("????????? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("?????????_????????????_??????_????????????_????????????")
    @Test
    void ????????????_????????????() throws Exception {
        // given
        Member member = MemberCreator.create("nickname1");
        memberRepository.save(member);
        Board board = BoardCreator.create(member.getId(), "title1", "content1");
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
                            parameterWithName("id").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardId").description("boardId"),
                                fieldWithPath("data.memberId").description("????????? ??????"),
                                fieldWithPath("data.petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.title").description("?????? ????????? ??????"),
                                fieldWithPath("data.petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.content").description("?????? ????????? ???"),
                                fieldWithPath("data.petitionUrl").description("url"),
                                fieldWithPath("data.petitionsCount").description("?????? ???"),
                                fieldWithPath("data.category").description("?????? ????????????"),
                                fieldWithPath("data.petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.viewCounts").description("?????????"),
                                fieldWithPath("data.boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.createdDate").description("????????????"),
                                fieldWithPath("data.memberResponse.memberId").description("????????? Id"),
                                fieldWithPath("data.memberResponse.name").description("????????? ??????"),
                                fieldWithPath("data.memberResponse.email").description("????????? ?????????"),
                                fieldWithPath("data.memberResponse.picture").description("????????? ??????"),
                                fieldWithPath("data.memberResponse.nickName").description("????????? ?????????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_????????????() throws Exception {
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
                                parameterWithName("search").description("????????? ?????? ????????? [required] required ????????? ???????????"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("????????? ??????"),
                                fieldWithPath("data.boardList[].petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.boardList[].content").description("?????? ????????? ???"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("?????? ???"),
                                fieldWithPath("data.boardList[].category").description("?????? ????????????"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].viewCounts").description("?????????"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].createdDate").description("????????????"),
                                fieldWithPath("data.boardList[].memberResponse").description("????????? ??????"),
                                fieldWithPath("data.boardCounts").description("????????? ??? ??????"),
                                fieldWithPath("data.boardPages").description("????????? ????????? ????????? ??? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_????????????_?????????() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=1&size=10&sort=viewCounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("????????? ?????? ????????? [required] required ????????? ???????????"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10"),
                                parameterWithName("sort").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("????????? ??????"),
                                fieldWithPath("data.boardList[].petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.boardList[].content").description("?????? ????????? ???"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("?????? ???"),
                                fieldWithPath("data.boardList[].category").description("?????? ????????????"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].viewCounts").description("?????????"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].createdDate").description("????????????"),
                                fieldWithPath("data.boardList[].memberResponse").description("????????? ??????"),
                                fieldWithPath("data.boardCounts").description("????????? ??? ??????"),
                                fieldWithPath("data.boardPages").description("????????? ????????? ????????? ??? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_????????????_?????????() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1");
        Board board2 = BoardCreator.create(1L, "title2", "content2");
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=1&size=10&sort=likeCounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("????????? ?????? ????????? [required] required ????????? ???????????"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10"),
                                parameterWithName("sort").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("????????? ??????"),
                                fieldWithPath("data.boardList[].petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.boardList[].content").description("?????? ????????? ???"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("?????? ???"),
                                fieldWithPath("data.boardList[].category").description("?????? ????????????"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].viewCounts").description("?????????"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].createdDate").description("????????????"),
                                fieldWithPath("data.boardList[].memberResponse").description("????????? ??????"),
                                fieldWithPath("data.boardCounts").description("????????? ??? ??????"),
                                fieldWithPath("data.boardPages").description("????????? ????????? ????????? ??? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_????????????2() throws Exception {
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
                                parameterWithName("search").description("????????? ?????? ????????? [required] required ????????? ???????????"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10"),
                                parameterWithName("sort").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("????????? ??????"),
                                fieldWithPath("data.boardList[].petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.boardList[].content").description("?????? ????????? ???"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("?????? ???"),
                                fieldWithPath("data.boardList[].category").description("?????? ????????????"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].viewCounts").description("?????????"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].createdDate").description("????????????"),
                                fieldWithPath("data.boardList[].memberResponse").description("????????? ??????"),
                                fieldWithPath("data.boardCounts").description("????????? ??? ??????"),
                                fieldWithPath("data.boardPages").description("????????? ????????? ????????? ??? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????????????????_????????????_??????????????????() throws Exception {
        // given
        Board board1 = BoardCreator.create(1L, "title1", "content1", BoardCategory.HEALTH);
        Board board2 = BoardCreator.create(1L, "title2", "content2", BoardCategory.HUMAN);
        boardRepository.saveAll(Arrays.asList(board1, board2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/getBoard/list?search=&page=1&size=10&category=HUMAN")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("board/list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("search").description("????????? ?????? ????????? [required] required ????????? ???????????"),
                                parameterWithName("page").description("page [required]"),
                                parameterWithName("size").description("size [optional] default = 10"),
                                parameterWithName("category").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data.boardList[].boardId").description("boardId"),
                                fieldWithPath("data.boardList[].memberId").description("????????? ??????"),
                                fieldWithPath("data.boardList[].petitionTitle").description("????????? ?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].title").description("?????? ????????? ??????"),
                                fieldWithPath("data.boardList[].petitionContent").description("??????????????? ????????? ???"),
                                fieldWithPath("data.boardList[].content").description("?????? ????????? ???"),
                                fieldWithPath("data.boardList[].petitionUrl").description("url"),
                                fieldWithPath("data.boardList[].petitionsCount").description("?????? ???"),
                                fieldWithPath("data.boardList[].category").description("?????? ????????????"),
                                fieldWithPath("data.boardList[].petitionCreatedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].petitionFinishedAt").description("?????? ?????????"),
                                fieldWithPath("data.boardList[].viewCounts").description("?????????"),
                                fieldWithPath("data.boardList[].boardCommentCounts").description("?????? ??????"),
                                fieldWithPath("data.boardList[].boardLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].boardUnLikeCounts").description("????????? ??????"),
                                fieldWithPath("data.boardList[].createdDate").description("????????????"),
                                fieldWithPath("data.boardList[].memberResponse").description("????????? ??????"),
                                fieldWithPath("data.boardCounts").description("????????? ??? ??????"),
                                fieldWithPath("data.boardPages").description("????????? ????????? ????????? ??? ??????")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void ?????????_??????_??????() throws Exception {
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
                                headerWithName("Authorization").description("??????")
                        ),
                        requestFields(
                                fieldWithPath("boardId").description("????????? ?????????"),
                                fieldWithPath("boardState").description("????????? ??????/??????")
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
    void ?????????_??????_??????_??????() throws Exception {
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
                                headerWithName("Authorization").description("??????")
                        ),
                        pathParameters(
                                parameterWithName("boardId").description("????????? ?????????")
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
    void ?????????_??????_??????_??????_????????????() throws Exception {
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
                                headerWithName("Authorization").description("??????")
                        ),
                        pathParameters(
                                parameterWithName("boardId").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code"),
                                fieldWithPath("message").description("message"),
                                fieldWithPath("data").description("??????????????? ???????????? ?????? ?????? ????????? null")
                        )
                ));
        resultActions.andExpect(status().isOk());
    }

}

