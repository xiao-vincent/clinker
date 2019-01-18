package com.vince.retailmanager.model.entity.fees;

import com.vince.retailmanager.model.entity.fees.FeeType.Constants;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Constants.ROYALTY)
@NoArgsConstructor
public class Royalty extends PercentageFee {

  private Royalty(String description, IncomeStatement incomeStatement) {
    super(description, incomeStatement);
  }

  public static Royalty create(IncomeStatement incomeStatement) {
    return new Royalty("royalty fee", incomeStatement);
  }
}
