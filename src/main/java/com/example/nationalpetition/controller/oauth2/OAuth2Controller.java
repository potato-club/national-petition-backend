package com.example.nationalpetition.controller.oauth2;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.domain.member.entity.Member;
import com.example.nationalpetition.dto.member.request.NickNameRequest;
import com.example.nationalpetition.dto.member.response.MemberCreateResponse;
import com.example.nationalpetition.exception.JwtTokenException;
import com.example.nationalpetition.security.jwt.Token;
import com.example.nationalpetition.security.jwt.TokenService;
import com.example.nationalpetition.security.oauth2.OAuth2ServiceUtils;
import com.example.nationalpetition.service.member.MemberService;
import com.example.nationalpetition.utils.ValidationUtils;
import com.example.nationalpetition.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OAuth2Controller {

    private final TokenService tokenService;
    private final MemberService memberService;

    /**
     * 테스트용 삭제예정
     * @return
     */
    @GetMapping("/nickName")
    public Long inputNickName(@RequestParam String token) {
        return tokenService.validateTokenAndGetMemberId(token);
    }

    @PostMapping("/mypage/nickName")
    public ApiResponse<MemberCreateResponse> addNickName(@RequestHeader("Authorization") String token,
                                                            @RequestBody @Valid NickNameRequest request, BindingResult bindingResult) throws BindException {
        ValidationUtils.validateBindingResult(bindingResult);
        final Member savedMember = memberService.addNickName(tokenService.validateTokenAndGetMemberId(token), request);
        return ApiResponse.success(MemberCreateResponse.of(savedMember));
    }

    @GetMapping("token/expired")
    public ResponseEntity<String> expired() {
        throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXCEPTION_EXPIRED);
    }


    @GetMapping("token/refresh")
    public ApiResponse<Token> refreshOAuth2(@RequestHeader("Refresh") String refreshToken, HttpServletResponse response) {

        OAuth2ServiceUtils.validateRefreshToken(tokenService, refreshToken);

        final Long memberId = tokenService.getMemberId(refreshToken);
        final Token newToken = tokenService.generateToken(memberId);

        OAuth2ServiceUtils.writeTokenResponse(response, newToken);

        return ApiResponse.success(newToken);
    }
}
