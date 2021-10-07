package com.example.nationalpetition.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldErrorDetail {

    private String message;

    public static FieldErrorDetail of(FieldError fieldError, MessageSource messageSource) {
        return new FieldErrorDetail(messageSource.getMessage(fieldError, Locale.KOREA));
    }


}
