package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.domain.board.Board;
import com.example.nationalpetition.domain.board.repository.BoardRepository;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.testObject.BoardCreator;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ConflictException;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindException;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriHost = "3.36.186.100")
@AutoConfigureMockMvc
class MemberControllerTest {

	@Autowired
	TokenService tokenService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	BoardRepository boardRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@AfterEach
	void clear() {
		memberRepository.deleteAll();
		boardRepository.deleteAll();
	}


	@Test
	@DisplayName("?????? ?????? ?????? (??????????????? ??????)")
	void getMyInfo() throws Exception {
		//given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		//when
		final ResultActions resultActions = mockMvc
				.perform(
						get("/api/v1/mypage/info")
								.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("mypage/info",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("????????? Id"),
								fieldWithPath("data.name").type(JsonFieldType.STRING).description("??????"),
								fieldWithPath("data.email").type(JsonFieldType.STRING).description("?????????"),
								fieldWithPath("data.picture").type(JsonFieldType.STRING).description("???????????????"),
								fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("?????????")
						)
						)
				);
		//then
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("?????? ?????? ?????? (?????? ??? ????????? ??????) -> ????????? ?????? ??????")
	void getMyInfo2() throws Exception {
		//given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId + 1L);
		//when then
        mockMvc.perform(
                get("/api/v1/mypage/info")
								.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("mypage/info/notFound"))
				.andExpect(result -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(NotFoundException.class)))
				.andReturn();

	}

	@Test
	@DisplayName("?????? ????????? ??????")
	void addNickName() throws Exception {
	    //given
		final Member member = memberRepository.save(Member.of("??????", "dd@dd.dd", "fdf"));
		final Token token = tokenService.generateToken(member.getId());
		final NickNameRequest request = new NickNameRequest("?????????");
		//when
		final ResultActions resultActions = mockMvc
				.perform(post("/api/v1/mypage/nickName")
				       .header("Authorization", "Bearer ".concat(token.getToken()))
					   .content(objectMapper.writeValueAsString(request))
					   .contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andDo(document("member/addNickName",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data").type(JsonFieldType.STRING).description("data")

						)
				)
			);
	    //then
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("?????? ????????? ?????? ?????? -> ????????? ??????")
	void addNickName_fail1() throws Exception {
		//given
		MemberServiceUtils.saveMember(memberRepository);
		final Member member = memberRepository.save(Member.of("?????????", "eee@ee.ee", "piiicture"));
		final Token token = tokenService.generateToken(member.getId());
		final NickNameRequest request = new NickNameRequest("?????????");
		//when
		final ResultActions resultActions = mockMvc
				.perform(post("/api/v1/mypage/nickName")
						.header("Authorization", "Bearer ".concat(token.getToken()))
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andDo(document("member/addNickName/Duplicate",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data").type(JsonFieldType.STRING).description("data")
						)
					)
				);
		//then
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("?????? ????????? ?????? ?????? -> ?????? ???????????? ????????? ??????")
	void addNickName_fail2() throws Exception {
	    //given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		final NickNameRequest request = new NickNameRequest("?????????22");
		//when
		final ResultActions resultActions = mockMvc
				.perform(post("/api/v1/mypage/nickName")
						.header("Authorization", "Bearer ".concat(token.getToken()))
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andDo(document("member/addNickName/AlreadyExist",
						preprocessResponse(prettyPrint())))
				.andExpect(result -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(ConflictException.class)));
		//then
		resultActions.andExpect(status().isConflict());
	}

	@Test
	@DisplayName("?????? ????????? ?????? ?????? --> ???????????? @Valid ????????????")
	void addNickName_fail3() throws Exception {
	    //given
		final Member member = memberRepository.save(Member.of("?????????", "eee@ee.ee", "picture"));
		final Token token = tokenService.generateToken(member.getId());
		final NickNameRequest request = new NickNameRequest();
		//when
		mockMvc
				.perform(post("/api/v1/mypage/nickName")
						.header("Authorization", "Bearer ".concat(token.getToken()))
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andDo(document("member/addNickName/Valid",
						preprocessResponse(prettyPrint())))
				.andExpect(result -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(BindException.class)));
	}

	@Test
	@DisplayName("??????????????? - ?????? ??? ????????? ??????")
	void getMyBoardList() throws Exception {
	    //given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		Board board1 = BoardCreator.create(memberId, "title1", "content1");
		Board board2 = BoardCreator.create(memberId, "title2", "content2");
		boardRepository.saveAll(Arrays.asList(board1, board2));

		//when
		final ResultActions resultActions = mockMvc
				.perform(get("/api/v1/mypage/boardList?page=1&size=1")
						.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("member/boardList",
						preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("page").description("????????? ?????? 1?????? ??????"),
								parameterWithName("size").description("????????? ????????? ?????? ")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data.myBoardList[].boardId").type(JsonFieldType.NUMBER).description("????????? Id"),
								fieldWithPath("data.myBoardList[].memberId").type(JsonFieldType.NUMBER).description("?????? Id"),
								fieldWithPath("data.myBoardList[].petitionTitle").type(JsonFieldType.STRING).description("?????? ??????"),
								fieldWithPath("data.myBoardList[].title").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
								fieldWithPath("data.myBoardList[].content").type(JsonFieldType.STRING).description("?????? ????????? ???"),
								fieldWithPath("data.myBoardList[].category").type(JsonFieldType.STRING).description("?????? ????????????"),
								fieldWithPath("data.myBoardList[].boardLikeCounts").type(JsonFieldType.NUMBER).description("????????? ??????"),
								fieldWithPath("data.myBoardList[].boardUnLikeCounts").type(JsonFieldType.NUMBER).description("????????? ??????"),
								fieldWithPath("data.myBoardList[].viewCounts").type(JsonFieldType.NUMBER).description("?????????"),
								fieldWithPath("data.myBoardList[].boardCommentCounts").type(JsonFieldType.NUMBER).description("?????? ??????"),
								fieldWithPath("data.myBoardList[].createdDate").type(JsonFieldType.STRING).description("?????????"),
								fieldWithPath("data.boardCounts").type(JsonFieldType.NUMBER).description("????????? ??? ??????")
						)));

		//then
		resultActions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("??????????????? - ?????? ??? ????????? ?????? - page ???????????? ??? ??????")
	void getMyBoardList_fail1() throws Exception {
		//given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		Board board1 = BoardCreator.create(memberId, "title1", "content1");
		Board board2 = BoardCreator.create(memberId, "title2", "content2");
		boardRepository.saveAll(Arrays.asList(board1, board2));

		//when
		final ResultActions resultActions = mockMvc
				.perform(get("/api/v1/mypage/boardList?page=0&size=10")
						.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("member/boardList/exception/page",
						preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("page").description("????????? ?????? 1?????? ??????"),
								parameterWithName("size").description("????????? ????????? ?????? ")
						),
						responseFields(
								fieldWithPath("errors[].message").description("code")
						)));

		//then
		resultActions.andExpect(status().is4xxClientError());
	}

	@Test
	@DisplayName("??????????????? - ?????? ??? ????????? ?????? - size ???????????? ??? ??????")
	void getMyBoardList_fail2() throws Exception {
		//given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		Board board1 = BoardCreator.create(memberId, "title1", "content1");
		Board board2 = BoardCreator.create(memberId, "title2", "content2");
		boardRepository.saveAll(Arrays.asList(board1, board2));

		//when
		final ResultActions resultActions = mockMvc
				.perform(get("/api/v1/mypage/boardList?page=1&size=0")
						.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("member/boardList/exception/page",
						preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("page").description("????????? ?????? 1?????? ??????"),
								parameterWithName("size").description("????????? ????????? ?????? ")
						),
						responseFields(
								fieldWithPath("errors[].message").description("code")
						)));

		//then
		resultActions.andExpect(status().is4xxClientError());
	}

	@Test
	@DisplayName("?????? ?????? ??????")
	void deleteMember() throws Exception {
	    //given
		final Long memberId = MemberServiceUtils.saveMember(memberRepository);
		final Token token = tokenService.generateToken(memberId);
		//when && then
		mockMvc
				.perform(delete("/api/v1/mypage/delete")
						.header("Authorization", "Bearer ".concat(token.getToken())))
				.andDo(print())
				.andDo(document("member/delete",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data").type(JsonFieldType.STRING).description("?????? ?????????")

						)
						)
				);
	}




}