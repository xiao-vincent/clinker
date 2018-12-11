package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Company extends BaseEntity {

	@JsonIgnore
	private boolean enabled = true;

	@JsonView(View.Summary.class)
	private String type = getClass().getSimpleName();

	@Column
	private BigDecimal cashBalance = BigDecimal.valueOf(0);

	@OneToMany(mappedBy = "recipient")
	@JsonIgnore
	private Set<Invoice> invoicesReceived = new HashSet<>();

	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	private Set<Invoice> invoicesSent = new HashSet<>();

	@OneToMany(mappedBy = "recipient")
	@JsonIgnore
	private Set<Payment> paymentsReceived = new HashSet<>();

	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	private Set<Payment> paymentsSent = new HashSet<>();

	@OneToMany(mappedBy = "company")
	@JsonIgnore
	private Set<IncomeStatement> incomeStatements = new HashSet<>();

	public void addInvoiceReceived(Invoice invoice) {
		invoicesReceived.add(invoice);
		invoice.setRecipient(this);
	}

	public void addInvoiceSent(Invoice invoice) {
		invoicesSent.add(invoice);
		invoice.setSender(this);
	}

	public void addPaymentReceived(Payment payment) {
		paymentsReceived.add(payment);
		payment.setRecipient(this);
	}

	public void addPaymentSent(Payment payment) {
		paymentsSent.add(payment);
		payment.setSender(this);
	}


	public void addIncomeStatement(IncomeStatement incomeStatement) {
		incomeStatements.add(incomeStatement);
		incomeStatement.setCompany(this);
		setCashBalance(
			 this.getCashBalance()
					.add(incomeStatement.getNetIncome())
		);
	}

}
