package com.example.nationalpetition.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationResult {

    private List<FieldErrorDetail> errors;

    public static ValidationResult error(Errors errors, MessageSource messageSource) {
        return new ValidationResult(
                errors.getFieldErrors().stream().map(error -> FieldErrorDetail.of(error, messageSource)).collect(Collectors.toList())
        );

    }
}
