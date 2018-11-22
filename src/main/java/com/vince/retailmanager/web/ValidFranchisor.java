package com.vince.retailmanager.web;

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
	String message() default "total price must be 50 or greater for online order. ";

	//		+ "Found: ${validatedValue.totalPrice}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

