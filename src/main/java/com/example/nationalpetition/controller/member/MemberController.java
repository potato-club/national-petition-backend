package com.example.nationalpetition.controller.member;

import com.example.nationalpetition.config.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberResponse;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.utils.ValidationUtils;
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

	@GetMapping("/api/v1/mypage/info")
	private ApiResponse<MemberResponse> getMyInfo(@MemberId Long memberId) {
		return ApiResponse.success(MemberResponse.of(memberService.findById(memberId)));
	}

	@PostMapping("/api/v1/mypage/nickName")
	public ApiResponse<MemberResponse> addNickName(@MemberId Long memberId,
												   @RequestBody @Valid NickNameRequest request, BindingResult bindingResult) throws BindException {
		ValidationUtils.validateBindingResult(bindingResult);
		final Member savedMember = memberService.addNickName(memberId, request);
		return ApiResponse.success(MemberResponse.of(savedMember));
	}

}
