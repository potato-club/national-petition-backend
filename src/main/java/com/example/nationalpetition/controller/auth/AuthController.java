package com.example.nationalpetition.controller.auth;

import com.example.nationalpetition.config.auth.Auth;
import com.example.nationalpetition.config.auth.MemberId;
import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.service.auth.AuthService;
import com.example.nationalpetition.service.auth.dto.request.RefreshTokenRequest;
import com.example.nationalpetition.service.auth.dto.response.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	@PostMapping("/api/v1/refresh/token")
	public ApiResponse<RefreshTokenResponse> refreshIdToken(@Valid @RequestBody RefreshTokenRequest request) {
		return ApiResponse.success(authService.refreshIdToken(request.getRefreshToken()));
	}

	@Auth
	@PostMapping("/api/v1/logout")
	public ApiResponse<String> logout(@MemberId Long memberId) {
		authService.logout(memberId);
		return ApiResponse.OK;
	}

}
