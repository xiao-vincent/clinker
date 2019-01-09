package com.vince.retailmanager.utils;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValidatorUtils {

  private Validator validator;

  public void validate(Object object,
      Class<?>... groups) {
    Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
