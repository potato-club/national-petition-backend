package com.example.nationalpetition.security.jwt;

import com.example.nationalpetition.utils.error.exception.JwtTokenException;
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

	@Value("${jwt.secret}")
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public Token generateToken(Long memberId) {
		// 토큰 인증시간 = 30분, refresh 토큰 만료 시간 = 3주
		final long tokenPeriod = 1000L * 60L * 30L;
		final long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L;

		final Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
		final Date now = new Date();

		return new Token(
				Jwts.builder()
						.setClaims(claims)
						.setIssuedAt(now)
						.setExpiration(new Date(now.getTime() + tokenPeriod))
						.signWith(SignatureAlgorithm.HS256, secretKey)
						.compact(),
				Jwts.builder()
						.setClaims(claims)
						.setIssuedAt(now)
						.setExpiration(new Date(now.getTime() + refreshPeriod))
						.signWith(SignatureAlgorithm.HS256, secretKey)
						.compact()
		);
	}

	public Long getMemberIdFromToken(String token) {
		try {
			return Long.valueOf(Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject());
		} catch (MalformedJwtException  | UnsupportedJwtException | IllegalArgumentException e) {
			log.info(MessageType.TOKEN_INVALID.getMessage() + e);
			throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_INVALID);
		} catch (ExpiredJwtException e) {
			throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_EXPIRED);
		}
	}

}
