package com.example.nationalpetition.utils.error.controllerAdvice;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.*;
import com.example.nationalpetition.utils.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    public ExceptionControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiResponse<Object> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(JwtTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ApiResponse<Object> handleJwtTokenException(JwtTokenException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ValidationResult handleBindException(BindException bindException) {
        return ValidationResult.error(bindException, messageSource);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(CONFLICT)
    protected ApiResponse<Object> handleDuplicateException(DuplicateException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    protected ApiResponse<Object> handleAlreadyExistException(AlreadyExistException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(BAD_GATEWAY)
    protected ApiResponse<Object> handleBadGatewayException(BadGatewayException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResponse<Object> handleValidationException(ValidationException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ApiResponse<Object> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        return ApiResponse.error(e.getError().getErrorCode(), e.getMessage());
    }
}
