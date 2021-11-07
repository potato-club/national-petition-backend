package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.member.request.AlarmRequest;
import com.example.nationalpetition.dto.member.request.MemberPageRequest;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.dto.member.response.MyPageBoardListResponse;
import com.example.nationalpetition.security.oauth2.OAuth2Dto;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


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
	@Auth
	@GetMapping("/api/v1/mypage/info")
	private ApiResponse<MemberResponse> getMyInfo(@MemberId Long memberId) {
		return ApiResponse.success(memberService.findById(memberId));
	}

	@Operation(summary = "닉네임을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@Auth
	@PostMapping("/api/v1/mypage/nickName")
	public ApiResponse<String> addNickName(@MemberId Long memberId,
												   @RequestBody @Valid NickNameRequest request, BindingResult bindingResult) throws BindException {
		ValidationUtils.validateBindingResult(bindingResult);
		return ApiResponse.success(memberService.addNickName(memberId, request));
	}

	@Operation(summary = "마이페이지 - 내가 작성한 글 리스트 조회 API", security = {@SecurityRequirement(name = "BearerKey")})
	@Auth
	@GetMapping("/api/v1/mypage/boardList")
	public ApiResponse<MyPageBoardListResponse> getMyBoardList(@MemberId Long memberId, @Valid MemberPageRequest request, BindingResult bindingResult) throws BindException {
		ValidationUtils.validateBindingResult(bindingResult);
		return ApiResponse.success(memberService.getMyBoardList(memberId, request));
	}

	@Operation(summary = "회원을 탈퇴하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@Auth
	@DeleteMapping("/api/v1/mypage/delete")
	public ApiResponse<String> deleteMember(@MemberId Long memberId) {
		return ApiResponse.success(memberService.deleteMember(memberId));
	}

	@Operation(summary = "게시글 알람을 구독 또는 해제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@Auth
	@PostMapping("/api/v1/mypage/changeBoardAlarm")
	public ApiResponse<String> changeBoardAlarm(@MemberId Long memberId, @RequestBody AlarmRequest request)  {
		memberService.changeBoardAlarm(memberId, request);
		return ApiResponse.OK;
	}

	@Operation(summary = "댓글 알람을 구독 또는 해제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@Auth
	@PostMapping("/api/v1/mypage/changeCommentAlarm")
	public ApiResponse<String> changeCommentAlarm(@MemberId Long memberId, @RequestBody AlarmRequest request)  {
		memberService.changeCommentAlarm(memberId, request);
		return ApiResponse.OK;
	}
}
