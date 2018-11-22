package com.vince.retailmanager.web;

import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.repository.FranchisorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.BiFunction;

public class FranchisorValidator implements ConstraintValidator<ValidFranchisor, Franchisor> {

	@Autowired
	private FranchisorRepository franchisorRepo;

	@Override
	public void initialize(ValidFranchisor constraintAnnotation) {
	}

	@Override
	public boolean isValid(Franchisor franchisor,
	                       ConstraintValidatorContext context) {
		if (franchisor == null) return true;

		boolean isValid = applyValidators(franchisor, context,
			 this::isNameValid,
			 this::isWebsiteValid
		);

		if (!isValid) {
			context.disableDefaultConstraintViolation();
		}
		return isValid;
	}

//	private boolean isDisableValid(Franchisor franchisor, ConstraintValidatorContext context) {
//		System.out.println("VALIDATING DISABLED");
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
			addExistsViolation(context, "name");
			return false;
		}
		return true;
	}


	private boolean isWebsiteValid(Franchisor franchisor, ConstraintValidatorContext context) {
		if (franchisorRepo.existsByWebsiteIgnoreCase(franchisor.getWebsite())) {
			addExistsViolation(context, "website");
			return false;
		}
		return true;
	}

	private void addExistsViolation(ConstraintValidatorContext context, String name) {
		context
			 .buildConstraintViolationWithTemplate(name + " already exists")
			 .addPropertyNode(name)
			 .addConstraintViolation();
	}

	@SafeVarargs
	private final boolean applyValidators(Franchisor franchisor, ConstraintValidatorContext context,
	                                      BiFunction<Franchisor, ConstraintValidatorContext, Boolean>... fns) {
		boolean isValid = true;
		for (BiFunction<Franchisor, ConstraintValidatorContext, Boolean> fn : fns) {
			if (!fn.apply(franchisor, context)) {
				isValid = false;
			}
		}
		return isValid;
	}

}


