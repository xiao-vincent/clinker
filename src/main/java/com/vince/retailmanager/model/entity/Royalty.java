package com.vince.retailmanager.model.entity;

import com.vince.retailmanager.model.entity.PercentageFee.FeeType.Constants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Constants.ROYALTY)
@NoArgsConstructor
public class Royalty extends PercentageFee {

//	private Royalty(IncomeStatement incomeStatement) {
//		super(incomeStatement);
//	}

  public static Royalty create(IncomeStatement incomeStatement) {
    Royalty royalty = new Royalty();
    royalty.setAttributes(incomeStatement);
    royalty.setDescription("royalty fee");
    royalty.setFeePercent(royalty.getFranchisor().getRoyaltyFeePercent());

    royalty.init();
    return royalty;
  }

//	private void init() {
//		this.setDefaultInvoice();
//	}
//	@Override
//	public String getDescription() {
//		return "royalty fee";
//	}

//	@Override
//	public double getFeePercent() {
//		return getFranchisor().getRoyaltyFeePercent();
//	}
}
