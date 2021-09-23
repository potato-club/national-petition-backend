package com.example.nationalpetition.error.controllerAdvice;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.error.errorCode.ErrorCode;
import com.example.nationalpetition.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> handleBusinessException(BusinessException e) {
        log.error(e.getMessage());
        return ApiResponse.error(e.getErrorCode().getCode(), e.getMessage());
    }

}
