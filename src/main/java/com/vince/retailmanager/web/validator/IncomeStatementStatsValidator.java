package com.vince.retailmanager.web.validator;

import com.vince.retailmanager.model.entity.financials.IncomeStatementStatistics;
import com.vince.retailmanager.model.entity.financials.utils.IncomeStatementUtils;
import com.vince.retailmanager.service.FinancialService;
import java.time.LocalDate;
import java.util.Collection;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class IncomeStatementStatsValidator implements
    ConstraintValidator<ValidIncomeStatementStats, IncomeStatementStatistics> {

  @Autowired
  private FinancialService financialService;


  @Override
  public void initialize(ValidIncomeStatementStats constraintAnnotation) {
  }

  @Override
  public boolean isValid(
      IncomeStatementStatistics incomeStatementStatistics,
      ConstraintValidatorContext context) {
    if (incomeStatementStatistics == null) {
      return true;
    }

    boolean isValid = ValidatorUtils.isValid(incomeStatementStatistics, context,
        this::noMissingStatementsInDateRange
    );

    if (!isValid) {
      context.disableDefaultConstraintViolation();
    }
    return isValid;
  }

  private boolean noMissingStatementsInDateRange(
      IncomeStatementStatistics incomeStatementStatistics,
      ConstraintValidatorContext context) {
    Collection<LocalDate> missingDates = IncomeStatementUtils.getMissingIncomeStatementDates(
        incomeStatementStatistics.getCompany(),
        incomeStatementStatistics.getDateRange()
    );
    if (!missingDates.isEmpty()) {
      System.out.println("what?");
      context.buildConstraintViolationWithTemplate("missing" + missingDates)
          .addPropertyNode("incomeStatements")
          .addConstraintViolation();
      return false;
    }
    return true;
  }

//	private boolean isDisableValid(IncomeStatementStatistics incomeStatementStats, ConstraintValidatorContext context) {
//		System.out.println("VALIDATING DISABLED");
//		if (!incomeStatementStats.getFranchisees().isEmpty()) {
//			context
//				 .buildConstraintViolationWithTemplate("franchisees rely on this incomeStatementStats")
//				 .addPropertyNode("franchisees")
//				 .addConstraintViolation();
//			return false;
//		}
//		return true;
//	}


}


