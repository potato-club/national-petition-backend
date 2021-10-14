package com.example.nationalpetition.config.validator;

import com.example.nationalpetition.utils.error.ErrorCode;
import com.example.nationalpetition.utils.error.exception.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PetitionUrlValidator implements ConstraintValidator<PetitionUrl, String> {

	private final static String exp = "^((http|https)://)?(www1.)(president.)(go.)(kr/)(petitions/)([0-9]+)(/)?";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			throw new ValidationException("청원 URL은 null이 될 수 없습니다.", ErrorCode.VALIDATION_EXCEPTION);
		}
		return Pattern.matches(exp, value);
	}

}
