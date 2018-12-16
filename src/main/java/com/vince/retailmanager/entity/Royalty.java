package com.vince.retailmanager.entity;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("royalty")
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
