package com.vince.retailmanager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "income_statements")
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeStatement extends BaseEntity {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "company")
	@NotNull(groups = {Validation.Entity.class})
	private Company company;

	@NotNull
	private BigDecimal sales;

	@NotNull
	private BigDecimal costOfGoodsSold;

	@Transient
	private BigDecimal grossProfit;

	@NotNull
	private BigDecimal operatingExpenses;

	@Transient
	private BigDecimal operatingIncome;

	@NotNull
	private BigDecimal generalAndAdminExpenses;

	@Transient
	private BigDecimal netIncome;

	@NotNull(groups = {Validation.Entity.class})
	private LocalDate date;

	public BigDecimal getGrossProfit() {
		return sales.subtract(costOfGoodsSold);
	}

	public BigDecimal getOperatingIncome() {
		return getGrossProfit().subtract(operatingExpenses);
	}

	public BigDecimal getNetIncome() {
		return getOperatingIncome().subtract(generalAndAdminExpenses);
	}

	public void setDate(int year, int month) {
		this.date = createEndOfMonthDate(year, month);
	}

	private static LocalDate createEndOfMonthDate(int year, int month) {
		return YearMonth.of(year, month).atEndOfMonth();
	}

	public static class IncomeStatementBuilder {
		public IncomeStatementBuilder sales(Double amt) {
			this.sales = BigDecimal.valueOf(amt);
			return this;
		}

		public IncomeStatementBuilder costOfGoodsSold(Double amt) {
			this.costOfGoodsSold = BigDecimal.valueOf(amt);
			return this;
		}

		public IncomeStatementBuilder operatingExpenses(Double amt) {
			this.operatingExpenses = BigDecimal.valueOf(amt);
			return this;
		}

		public IncomeStatementBuilder generalAndAdminExpenses(Double amt) {
			this.generalAndAdminExpenses = BigDecimal.valueOf(amt);
			return this;
		}

		public IncomeStatementBuilder date(int year, int month) {
			this.date = createEndOfMonthDate(year, month);
			return this;
		}
	}


}

