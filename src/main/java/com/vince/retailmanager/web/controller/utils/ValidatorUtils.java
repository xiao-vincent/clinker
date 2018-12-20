package com.vince.retailmanager.web.controller.utils;

import java.util.function.BiFunction;
import javax.validation.ConstraintValidatorContext;

public class ValidatorUtils {

  @SafeVarargs
  public static <T> boolean applyValidators(T object, ConstraintValidatorContext context,
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
