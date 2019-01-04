package com.vince.retailmanager.web.controller.utils;

import java.util.function.BiFunction;
import javax.validation.ConstraintValidatorContext;

public class ValidatorUtils {

  @SafeVarargs
  public static <T> boolean isValid(T object, ConstraintValidatorContext context,
      BiFunction<T, ConstraintValidatorContext, Boolean>... fns) {
    if (object == null) {
      return true;
    }

    boolean isValid = true;
    for (BiFunction<T, ConstraintValidatorContext, Boolean> fn : fns) {
      if (!fn.apply(object, context)) {
        isValid = false;
      }
    }

    if (!isValid) {
      context.disableDefaultConstraintViolation();
    }
    return isValid;
  }

  public static void addExistsViolation(ConstraintValidatorContext context, String name) {
    context
        .buildConstraintViolationWithTemplate(name + " already exists")
        .addPropertyNode(name)
        .addConstraintViolation();
  }
}
