package com.depromeet.annotation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VisitedDateValidator implements ConstraintValidator<VisitedDateFormat, String> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

	@Override
	public void initialize(VisitedDateFormat constraintAnnotation) {
		dateFormat.setLenient(false);
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		try {
			Date date = dateFormat.parse(value);
			return !date.after(new Date()); // 미래 날짜인지 확인
		} catch (ParseException e) {
			return false; // 날짜 형식이 유효하지 않음
		}
	}
}
