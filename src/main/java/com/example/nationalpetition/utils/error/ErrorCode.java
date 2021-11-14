package com.example.nationalpetition.utils.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Bad Gateway
	BAD_GATEWAY_EXCEPTION("B001", "외부 API 호출중에 애러가 발생했습니다."),

	// Not Found
	NOT_FOUND_EXCEPTION_USER("N001", "해당 하는 유저를 찾을 수 없습니다."),
	NOT_FOUND_EXCEPTION_BOARD("N002", "해당 하는 게시글을 찾을 수 없습니다."),
	NOT_FOUND_EXCEPTION_COMMENT("N003", "해당 하는 댓글을 찾을 수 없습니다."),
	NOT_FOUND_EXCEPTION_PETITION("N004", "해당 하는 청원 글을 찾을 수 없습니다."),
	NOT_FOUND_EXCEPTION_BOARD_LIKE("N005", "해당 하는 좋아요를 찾을 수 없습니다."),
	NOT_FOUND_EXCEPTION_NOTIFICATION("N006", "해당 하는 알림을 찾을 수 없습니다."),

	// UnAuthorized
	UNAUTHORIZED_EXCEPTION("U001", "잘못된 토큰입니다. 다시 로그인해주세요"),
	JWT_TOKEN_EXCEPTION_EXPIRED("U002", "JWT 토큰이 만료되었습니다."),

	// Bad Request
	VALIDATION_EXCEPTION("B001", "유효하지 않은 값입니다."),

	// Conflict
	CONFLICT_EXCEPTION("C001", "이미 존재하는 값입니다."),
	DUPLICATE_EXCEPTION_NICKNAME("C002", "닉네임이 중복됩니다."),
	ALREADY_EXIST_EXCEPTION_ADD_NICKNAME("C003", "이미 닉네임을 등록하셨습니다."),

	// Forbidden
	FORBIDDEN_EXCEPTION("F001", "접근할 수 없습니다."),
	FORBIDDEN_COMMENT_EXCEPTION("F002", "더 이상 댓글을 생성할 수 없습니다."),

	// Internal Server
	INTERNAL_SERVER_EXCEPTION("I001", "서버 내부에서 문제가 발생하였습니다"),
	;

	private final String code;
	private final String message;

}