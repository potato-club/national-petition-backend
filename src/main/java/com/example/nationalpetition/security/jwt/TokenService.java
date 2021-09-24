package com.example.nationalpetition.security.jwt;

import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.utils.error.exception.JwtTokenException;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.utils.error.ErrorCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final MemberService memberService;

    public TokenService(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(Long memberId) {
        // 토큰 인증시간 = 10분, refresh 토큰 만료 시간 = 3주
        final long tokenPeriod = 1000L * 60L * 10L;
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

    public boolean validateToken(String token) {

        try {
            token = removeBear(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Long getMemberId(String token) {
        token = removeBear(token);
        return Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
    }

    public Long validateTokenAndGetMemberId(String token) {
        if (validateToken(token)) {
            token = removeBear(token);
            return Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        }
        throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_INVALID);
    }


    public Authentication getAuthentication(String token) {
        final Long memberId = getMemberId(token);
        final Member member = memberService.findById(memberId);

        return new UsernamePasswordAuthenticationToken(member, token, null);
    }

    private String removeBear(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }


}
