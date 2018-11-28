package com.vince.retailmanager.web.controller.validator;

import javax.validation.ConstraintValidatorContext;
import java.util.function.BiFunction;

class ValidatorUtils {
	@SafeVarargs
	static <T> boolean applyValidators(T object, ConstraintValidatorContext context,
	                                   BiFunction<T, ConstraintValidatorContext, Boolean>... fns) {
		boolean isValid = true;
		for (BiFunction<T, ConstraintValidatorContext, Boolean> fn : fns) {
			if (!fn.apply(object, context)) {
				isValid = false;
			}
		}
		return isValid;
	}
}
