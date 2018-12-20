package com.vince.retailmanager.web.controller;

import com.vince.retailmanager.model.entity.Payment;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.web.controller.utils.ValidatorUtils;
import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentValidator implements ConstraintValidator<ValidPayment, Payment> {

  @Autowired
  private PaymentRepository paymentRepository;

  @Override
  public void initialize(ValidPayment constraintAnnotation) {
  }

  @Override
  public boolean isValid(Payment payment,
      ConstraintValidatorContext context) {
    if (payment == null) {
      return true;
    }

    boolean isValid = ValidatorUtils.applyValidators(payment, context,
        this::isPaymentOverRemainingBalance
    );

    if (!isValid) {
      context.disableDefaultConstraintViolation();
    }
    return isValid;
  }

  private boolean isPaymentOverRemainingBalance(Payment payment,
      ConstraintValidatorContext context) {
    BigDecimal paymentAmt = payment.getAmount();
    BigDecimal invoiceBalance = payment.getInvoice().getBalance().add(paymentAmt);

    if (paymentAmt.compareTo(invoiceBalance) > 0) {
      String msg =
          "Payment amount of " + paymentAmt + " is greater than remaining invoice balance of "
              + invoiceBalance;
      context.buildConstraintViolationWithTemplate(msg)
          .addPropertyNode("amount")
          .addConstraintViolation();
      return false;
    } else {
      return true;
    }
  }

//	private boolean isWebsiteValid(Payment payment, ConstraintValidatorContext context) {
//		if (paymentRepo.existsByWebsiteIgnoreCase(payment.getWebsite())) {
//			addExistsViolation(context, "website");
//			return false;
//		}
//		return true;
//	}
//
//	private void addExistsViolation(ConstraintValidatorContext context, String name) {
//		context
//			 .buildConstraintViolationWithTemplate(name + " already exists")
//			 .addPropertyNode(name)
//			 .addConstraintViolation();
//	}

//	@SafeVarargs
//	private final boolean applyValidators(Payment payment, ConstraintValidatorContext context,
//	                                      BiFunction<Payment, ConstraintValidatorContext, Boolean>... fns) {
//		boolean isValid = true;
//		for (BiFunction<Payment, ConstraintValidatorContext, Boolean> fn : fns) {
//			if (!fn.apply(payment, context)) {
//				isValid = false;
//			}
//		}
//		return isValid;
//	}

}


