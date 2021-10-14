package com.example.nationalpetition.service.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenResponse {

	private final String idToken;

}
