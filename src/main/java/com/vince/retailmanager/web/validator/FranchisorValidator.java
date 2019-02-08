package com.vince.retailmanager.web.validator;

import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.repository.FranchisorRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class FranchisorValidator implements ConstraintValidator<ValidFranchisor, Franchisor> {

  @Autowired
  private FranchisorRepository franchisorRepo;

  @Override
  public boolean isValid(Franchisor franchisor,
      ConstraintValidatorContext context) {
    return ValidatorUtils.isValid(franchisor, context,
        this::isNameValid,
        this::isWebsiteValid
    );
  }

  private boolean isNameValid(Franchisor franchisor, ConstraintValidatorContext context) {
    if (franchisorRepo.existsByNameIgnoreCase(franchisor.getName())) {
      ValidatorUtils.addExistsViolation(context, "name");
      return false;
    }
    return true;
  }


  private boolean isWebsiteValid(Franchisor franchisor, ConstraintValidatorContext context) {
    if (franchisorRepo.existsByWebsiteIgnoreCase(franchisor.getWebsite())) {
      ValidatorUtils.addExistsViolation(context, "website");
      return false;
    }
    return true;
  }


}


