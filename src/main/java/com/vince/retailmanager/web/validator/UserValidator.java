package com.vince.retailmanager.web.validator;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.repository.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserValidator implements ConstraintValidator<ValidUser, User> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean isValid(User user,
      ConstraintValidatorContext context) {
    return ValidatorUtils.isValid(user, context,
        this::isUsernameValid
    );
  }


  private boolean isUsernameValid(User user, ConstraintValidatorContext context) {
    if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
      ValidatorUtils.addExistsViolation(context, "username");
      return false;
    }
    return true;
  }

}

