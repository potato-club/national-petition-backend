package com.example.nationalpetition.controller.rank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(uriHost = "3.36.64.148")
@AutoConfigureMockMvc
@SpringBootTest
public class PetitionRankControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void 국민_청원_인기_랭킹을_조회한다() throws Exception {
		mockMvc.perform(get("/api/v1/petition/rank"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("petitionRank",
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING).description("code"),
								fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
								fieldWithPath("data.status").type(JsonFieldType.STRING).description("status"),
								fieldWithPath("data.item").type(JsonFieldType.ARRAY).description("item"),
								fieldWithPath("data.item[].id").type(JsonFieldType.STRING).description("청원 id"),
								fieldWithPath("data.item[].title").type(JsonFieldType.STRING).description("청원 제목"),
								fieldWithPath("data.item[].agreement").type(JsonFieldType.STRING).description("청원 수"),
								fieldWithPath("data.item[].category").type(JsonFieldType.STRING).description("카테고리"),
								fieldWithPath("data.item[].created").description("청원 시작 날짜"),
								fieldWithPath("data.item[].finished").description("청원 종료 날짜")
						)
				));
	}

}
