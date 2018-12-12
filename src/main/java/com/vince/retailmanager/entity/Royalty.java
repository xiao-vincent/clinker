package com.vince.retailmanager.entity;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("royalty")
@NoArgsConstructor
public class Royalty extends PercentageFee {

	public Royalty(IncomeStatement incomeStatement) {
		super(incomeStatement);
		setDescription("royalty fee");
		setFeePercent(getFranchisor().getRoyaltyFeePercent());
	}

//	@Override
//	public String getDescription() {
//		return "royalty fee";
//	}

//	@Override
//	public double getFeePercent() {
//		return getFranchisor().getRoyaltyFeePercent();
//	}
}
