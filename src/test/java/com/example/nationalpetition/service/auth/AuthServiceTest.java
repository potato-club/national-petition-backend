package com.example.nationalpetition.service.auth;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.domain.member.repository.MemberRepository;
import com.example.nationalpetition.service.auth.dto.response.RefreshTokenResponse;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import com.example.nationalpetition.utils.error.exception.UnAuthorizedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
	}

	@Test
	void RefreshToken을_이용해서_idToken을_재발급_받는다() {
		// given
		String refreshToken = "refreshToken";

		Member member = Member.of("name", "email@gmail.com", "https://picture.com");
		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);

		// when
		RefreshTokenResponse response = authService.refreshIdToken(refreshToken);

		// then
		assertThat(response.getIdToken().startsWith("ey")).isTrue();
	}

	@Test
	void 유효하지_않은_RefreshToken인경우_401에러발생() {
		// given
		String refreshToken = "InActive RefreshToken";

		// when & then
		assertThatThrownBy(() -> authService.refreshIdToken(refreshToken)).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	void 로그아웃되면_RefreshToken이_삭제된다() {
		// given
		String refreshToken = "refreshToken";

		Member member = Member.of("name", "email@gmail.com", "https://picture.com");
		member.updateRefreshToken(refreshToken);
		memberRepository.save(member);

		// when
		authService.logout(member.getId());

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertThat(members.get(0).getRefreshToken()).isNull();
	}

	@Test
	void 로그아웃시_존재하지_않은_유저면_404에러발생() {
		// given
		Long memberId = 120L;

		// when
		assertThatThrownBy(() -> authService.logout(memberId)).isInstanceOf(NotFoundException.class);
	}

}