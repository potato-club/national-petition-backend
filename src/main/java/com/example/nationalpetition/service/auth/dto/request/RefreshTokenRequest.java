package com.example.nationalpetition.service.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RefreshTokenRequest {

	@NotBlank(message = "refreshToken이 없습니다.")
	private String refreshToken;

}
