package com.vince.retailmanager.model.entity.fees;


import com.vince.retailmanager.model.entity.fees.FeeType.Constants;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * A marketing fee is paid into the franchise's marketing budget.
 *
 * @author Vincent Xiao
 */
@Entity
@DiscriminatorValue(Constants.MARKETING)
@NoArgsConstructor
public class MarketingFee extends PercentageFee {

  private MarketingFee(String description, IncomeStatement incomeStatement) {
    super(description, incomeStatement);
  }

  public static MarketingFee create(IncomeStatement incomeStatement) {
    return new MarketingFee("marketing fee", incomeStatement);
  }
}

