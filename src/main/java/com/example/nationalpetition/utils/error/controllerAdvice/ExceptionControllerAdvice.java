package com.example.nationalpetition.utils.error.controllerAdvice;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.utils.error.exception.*;
import com.example.nationalpetition.utils.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.nationalpetition.utils.error.ErrorCode.*;
import static com.example.nationalpetition.utils.error.ErrorCode.INTERNAL_SERVER_EXCEPTION;
import static com.example.nationalpetition.utils.error.ErrorCode.UNAUTHORIZED_EXCEPTION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice {

	private final MessageSource messageSource;

	/**
	 * 400 BadRequest
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiResponse<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(VALIDATION_EXCEPTION.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(BAD_REQUEST)
	protected ValidationResult handleBindException(BindException bindException) {
		log.error(bindException.getMessage());
		return ValidationResult.error(bindException, messageSource);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(BAD_REQUEST)
	protected ApiResponse<Object> handleValidationException(ValidationException e) {
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	/**
	 * 401
	 */
	@ExceptionHandler(UnAuthorizedException.class)
	@ResponseStatus(UNAUTHORIZED)
	protected ApiResponse<Object> handleJwtTokenException(UnAuthorizedException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	@ExceptionHandler(OAuth2AuthenticationException.class)
	@ResponseStatus(UNAUTHORIZED)
	protected ApiResponse<Object> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(UNAUTHORIZED_EXCEPTION.getCode(), UNAUTHORIZED_EXCEPTION.getMessage());
	}

	/**
	 * 403
	 */
	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(FORBIDDEN)
	protected  ApiResponse<Object> handleForbiddenException(ForbiddenException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	/**
	 * 404
	 */
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(NOT_FOUND)
	protected ApiResponse<Object> handleNotFoundException(NotFoundException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	/**
	 * 409
	 */
	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(CONFLICT)
	protected ApiResponse<Object> handleAlreadyExistException(ConflictException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	/**
	 * 503
	 */
	@ExceptionHandler(BadGatewayException.class)
	@ResponseStatus(BAD_GATEWAY)
	protected ApiResponse<Object> handleBadGatewayException(BadGatewayException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
	}

	/**
	 * 500
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	protected ApiResponse<Object> handleRuntimeException(RuntimeException e) {
		log.error(e.getMessage(), e);
		return ApiResponse.error(INTERNAL_SERVER_EXCEPTION.getCode(), INTERNAL_SERVER_EXCEPTION.getMessage());
	}

}
