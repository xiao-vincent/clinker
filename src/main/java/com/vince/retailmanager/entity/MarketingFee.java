package com.vince.retailmanager.entity;


import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("marketing_fee")
@NoArgsConstructor
public class MarketingFee extends PercentageFee {

	public MarketingFee(Franchisor franchisor, IncomeStatement incomeStatement) {
		super(franchisor, incomeStatement);
	}

	@Override
	public String getDescription() {
		return "marketing fee";
	}

	@Override
	public double getFeePercent() {
		return getFranchisor().getMarketingFeePercent();
	}
}
