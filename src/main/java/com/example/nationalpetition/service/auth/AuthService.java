package com.example.nationalpetition.service.auth;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.service.auth.dto.response.RefreshTokenResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.utils.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final MemberRepository memberRepository;
	private final TokenService tokenService;

	@Transactional(readOnly = true)
	public RefreshTokenResponse refreshIdToken(String refreshToken) {
		Member member = memberRepository.findByRefreshToken(refreshToken)
				.orElseThrow(() -> new UnAuthorizedException(String.format("잘못된 RefreshToken (%s)입니다.", refreshToken)));
		return new RefreshTokenResponse(tokenService.generateIdToken(member.getId()));
	}

	@Transactional
	public void logout(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException(String.format("해당하는 유저 (%s)는 존재하지 않습니다", memberId), ErrorCode.NOT_FOUND_EXCEPTION_USER));
		member.removeRefreshToken();
	}

}
