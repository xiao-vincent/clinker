package com.vince.retailmanager.web.controller.Franchisee;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.utils.StringUtils;
import com.vince.retailmanager.web.controller.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class FranchiseeValidator implements ConstraintValidator<ValidFranchisee, Franchisee> {

	@Autowired
	private FranchiseeRepository franchiseeRepository;

	@Override
	public void initialize(ValidFranchisee constraintAnnotation) {
	}

	@Override
	public boolean isValid(Franchisee franchisee,
	                       ConstraintValidatorContext context) {
		if (franchisee == null) return true;

		boolean isValid = ValidatorUtils.applyValidators(franchisee, context,
//			 this::isNameValid
			 this::minimumCashBalanceMet
		);

		if (!isValid) {
			context.disableDefaultConstraintViolation();
		}
		return isValid;
	}

	private boolean minimumCashBalanceMet(Franchisee franchisee, ConstraintValidatorContext context) {
		double minimum = franchisee.getFranchisor().getLiquidCapitalRequirement();
		BigDecimal balance = franchisee.getCashBalance();
		if (balance.doubleValue() < minimum) {
			context.
				 buildConstraintViolationWithTemplate(StringUtils.format(balance) +
						" does not meet franchisor's liquid capital requirements of " + StringUtils.format(minimum))
				 .addPropertyNode("cashBalance")
				 .addConstraintViolation();
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
//
//	@SafeVarargs
//	private final boolean applyValidators(Object object, ConstraintValidatorContext context,
//	                                      BiFunction<Object, ConstraintValidatorContext, Boolean>... fns) {
//		boolean isValid = true;
//		for (BiFunction<Object, ConstraintValidatorContext, Boolean> fn : fns) {
//			if (!fn.apply(object, context)) {
//				isValid = false;
//			}
//		}
//		return isValid;
//	}

}


