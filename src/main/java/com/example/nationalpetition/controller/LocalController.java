package com.example.nationalpetition.controller;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"local", "dev"})
@RequiredArgsConstructor
@RestController
public class LocalController {

	private final MemberRepository memberRepository;
	private final TokenService tokenService;

	@GetMapping("/test-token")
	public ApiResponse<Token> getTestToken() {
		Member member = memberRepository.findByEmail("potato.test@gmail.com")
				.orElse(Member.builder()
						.email("potato.test@gmail.com")
						.name("test")
						.picture("picture")
						.build()
				);
		memberRepository.save(member);
		return ApiResponse.success(tokenService.generateToken(member.getId()));
	}

}
