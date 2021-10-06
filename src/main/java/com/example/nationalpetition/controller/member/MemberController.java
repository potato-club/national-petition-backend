package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.config.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.board.response.BoardInfoResponseInMyPage;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.security.oauth2.OAuth2Dto;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.utils.message.MessageType;
import com.example.nationalpetition.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberService memberService;

	/**
	 * 테스트용
	 */
	@GetMapping("/nickName")
	public OAuth2Dto inputNickName(@RequestParam String register, @RequestParam String idToken, @RequestParam String refreshToken) {
		return OAuth2Dto.of(register, idToken, refreshToken);
	}

	@Operation(summary = "나의 회원 정보를 불러오는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@GetMapping("/api/v1/mypage/info")
	private ApiResponse<MemberResponse> getMyInfo(@MemberId Long memberId) {
		return ApiResponse.success(memberService.findById(memberId));
	}

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PostMapping("/api/v1/mypage/nickName")
	public ApiResponse<MessageType> addNickName(@MemberId Long memberId,
												   @RequestBody @Valid NickNameRequest request, BindingResult bindingResult) throws BindException {
		ValidationUtils.validateBindingResult(bindingResult);
		return ApiResponse.success(memberService.addNickName(memberId, request));
	}

	@Operation(summary = "마이페이지 - 내가 작성한 글 리스트 조회 API", security = {@SecurityRequirement(name = "BearerKey")})
	@GetMapping("/api/v1/mypage/boardList")
	public ApiResponse<List<BoardInfoResponseInMyPage>> getMyBoardList(@MemberId Long memberId,
																	   @PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
		return ApiResponse.success(memberService.getMyBoardList(memberId, pageable));
	}

	@Operation(summary = "회원을 탈퇴하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@DeleteMapping("/api/v1/mypage/delete")
	public ApiResponse<MessageType> deleteMember(@MemberId Long memberId) {
		return ApiResponse.success(memberService.deleteMember(memberId));
	}

}
