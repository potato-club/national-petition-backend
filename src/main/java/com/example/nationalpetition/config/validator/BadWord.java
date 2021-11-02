package com.example.nationalpetition.config.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BadWordValidator.class})
public @interface BadWord {

    String message() default "욕을 쓰지 맙시다~~";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
