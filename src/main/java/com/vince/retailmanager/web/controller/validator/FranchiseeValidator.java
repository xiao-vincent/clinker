package com.vince.retailmanager.web.controller.validator;

import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.utils.StringUtils;
import com.vince.retailmanager.web.controller.utils.ValidatorUtils;
import java.math.BigDecimal;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class FranchiseeValidator implements ConstraintValidator<ValidFranchisee, Franchisee> {

  @Autowired
  private FranchiseeRepository franchiseeRepository;

  @Override
  public boolean isValid(Franchisee franchisee,
      ConstraintValidatorContext context) {
    return ValidatorUtils.isValid(franchisee, context,
        this::minimumCashBalanceMet
    );
  }

  private boolean minimumCashBalanceMet(Franchisee franchisee, ConstraintValidatorContext context) {
    double minimum = franchisee.getFranchisor().getLiquidCapitalRequirement();
    BigDecimal balance = franchisee.getCashBalance();
    if (balance.doubleValue() < minimum) {
      String msg =
          StringUtils.format(balance) +
              " does not meet franchisor's liquid capital requirements of " + StringUtils
              .format(minimum);
      throw new InvalidOperationException(msg, Map.of("minimum", minimum));
    }
    return true;
  }
}



