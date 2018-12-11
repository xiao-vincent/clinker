package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "percentage_fees")
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fee_type")
public abstract class PercentageFee extends BaseEntity {


	public PercentageFee(Franchisor franchisor, IncomeStatement incomeStatement) {
		this.setFranchisor(franchisor);
		if (incomeStatement != null) {
			this.setIncomeStatement(incomeStatement);
			this.setFranchisee((Franchisee) incomeStatement.getCompany());
			this.setDefaultInvoice();
		}
	}


	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "franchisor_id")
	@NotNull
	@JsonView(View.Public.class)
	private Franchisor franchisor;


	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "franchisee_id")
	@NotNull
	@JsonView(View.Public.class)
	private Franchisee franchisee;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "income_statement_id")
	@NotNull
	@JsonView(View.Public.class)
	private IncomeStatement incomeStatement;

	@NotNull
	private double feePercent;

	private String description;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "invoice_id")
	@NotNull
	@Valid
	@JsonView(View.Public.class)
	private Invoice invoice;

	//get payment amount and insert into invoice
	public double getBaseAmount() {
		if (this.getIncomeStatement() == null) return 0.0;
		else
			return this.getIncomeStatement().getSales().doubleValue();
	}

	public double getFee() {
		return this.getFeePercent() * this.getBaseAmount();
	}

	public void setDefaultInvoice() {
		this.setInvoice(Invoice.builder()
			 .due(this.getFee())
			 .description(this.getDescription())
			 .sender(this.getFranchisor())
			 .recipient(this.getFranchisee())
			 .build());
	}
}



