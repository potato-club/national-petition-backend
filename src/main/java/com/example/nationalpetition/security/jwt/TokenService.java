package com.example.nationalpetition.security.jwt;

import com.example.nationalpetition.utils.error.exception.UnAuthorizedException;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.message.MessageType;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class TokenService {

	private static final long DAY = 1000 * 60 * 60 * 24;
	private static final long ACCESS_TOKEN_EXPIRED_TIME = DAY * 3; // AccessToken 만료시간: 3일
	private static final long REFRESH_TOKEN_EXPIRED_TIME = DAY * 30; // RefreshToken 만료시간: 30일

	@Value("${jwt.secret}")
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public Token generateToken(Long memberId) {
		final Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
		final Date now = new Date();

		String refreshToken = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_TIME))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();


		return new Token(
				Jwts.builder()
						.setClaims(claims)
						.setIssuedAt(now)
						.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME))
						.signWith(SignatureAlgorithm.HS256, secretKey)
						.compact(), refreshToken
		);
	}

	public Long getMemberIdFromToken(String token) {
		try {
			return Long.valueOf(Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject());
		} catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			log.info(MessageType.TOKEN_INVALID.getMessage() + e);
			throw new UnAuthorizedException(String.format("잘못된 토큰 (%s) 입니다.", token));
		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(String.format("만료된 토큰 (%s) 입니다", token), ErrorCode.JWT_TOKEN_EXCEPTION_EXPIRED);
		}
	}

	public String generateIdToken(Long memberId) {
		final long tokenPeriod = 1000L * 60L * 30L;
		final Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
		final Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenPeriod))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	public String generateRefreshToken() {
		final Date now = new Date();
		return Jwts.builder()
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_TIME))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();

	}

}
