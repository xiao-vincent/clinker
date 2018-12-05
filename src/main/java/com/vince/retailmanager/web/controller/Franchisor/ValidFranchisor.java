package com.vince.retailmanager.web.controller.Franchisor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FranchisorValidator.class)
@Documented
public @interface ValidFranchisor {
	String message() default "";

	//		+ "Found: ${validatedValue.totalPrice}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

