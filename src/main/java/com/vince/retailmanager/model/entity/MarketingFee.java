package com.vince.retailmanager.model.entity;


import com.vince.retailmanager.model.entity.PercentageFee.FeeType.Constants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

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

