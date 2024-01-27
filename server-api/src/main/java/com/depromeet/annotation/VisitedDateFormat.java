package com.depromeet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = VisitedDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitedDateFormat {
	String message() default "날짜 형식이 잘못되거나, 유효하지 않은 날짜를 입력했습니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
