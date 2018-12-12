package com.vince.retailmanager.entity;


import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("marketing_fee")
@NoArgsConstructor
public class MarketingFee extends PercentageFee {

	public MarketingFee(IncomeStatement incomeStatement) {
		super(incomeStatement);
		setDescription("marketing fee");
		setFeePercent(getFranchisor().getMarketingFeePercent());
	}

//	@Override
//	public String getDescription() {
//		return "marketing fee";
//	}
//
//	@Override
//	public double getFeePercent() {
//		return getFranchisor().getMarketingFeePercent();
//	}
}
