package com.vince.retailmanager.web.controller.validator;

import com.vince.retailmanager.model.entity.Franchisor;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.web.controller.utils.ValidatorUtils;
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

//	private boolean isDisableValid(Franchisor franchisor, ConstraintValidatorContext context) {
//		if (!franchisor.getFranchisees().isEmpty()) {
//			context
//				 .buildConstraintViolationWithTemplate("franchisees rely on this franchisor")
//				 .addPropertyNode("franchisees")
//				 .addConstraintViolation();
//			return false;
//		}
//		return true;
//	}

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

  //	@SafeVarargs
//	private final boolean isValid(Franchisor franchisor, ConstraintValidatorContext context,
//	                                      BiFunction<Franchisor, ConstraintValidatorContext, Boolean>... fns) {
//		boolean isValid = true;
//		for (BiFunction<Franchisor, ConstraintValidatorContext, Boolean> fn : fns) {
//			if (!fn.apply(franchisor, context)) {
//				isValid = false;
//			}
//		}
//		return isValid;
//	}

}


