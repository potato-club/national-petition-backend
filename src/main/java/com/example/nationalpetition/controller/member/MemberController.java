package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.config.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
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
	public String inputNickName(@RequestParam String token) {
		return token;
	}

	@Operation(summary = "나의 회원 정보를 불러오는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@GetMapping("/api/v1/mypage/info")
	private ApiResponse<MemberResponse> getMyInfo(@MemberId Long memberId) {
		return ApiResponse.success(memberService.findById(memberId));
	}

	@Operation(summary = "닉네임을 수정하는 API", security = {@SecurityRequirement(name = "BearerKey")})
	@PutMapping("/api/v1/mypage/nickName")
	public ApiResponse<MemberResponse> addNickName(@MemberId Long memberId,
												   @RequestBody @Valid NickNameRequest request, BindingResult bindingResult) throws BindException {
		ValidationUtils.validateBindingResult(bindingResult);
		return ApiResponse.success(memberService.addNickName(memberId, request));
	}
}
