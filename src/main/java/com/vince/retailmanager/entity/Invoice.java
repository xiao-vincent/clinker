package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "invoices")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

	@DecimalMin("0.00")
	private BigDecimal due;

	@JsonInclude
	private transient BigDecimal balance;

	//	@NotNull
	private String description;

	@OneToOne
	@JoinColumn(name = "seller_id")
	@NotNull
	private Company seller;

	@OneToOne
	@JoinColumn(name = "customer_id")
	@NotNull
	private Company customer;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	public BigDecimal getBalance() {
		if (payment == null) {
			return due;
		} else {
			return due.subtract(payment.getAmount());
		}
	}


	public static class InvoiceBuilder {
		public InvoiceBuilder balance(Double amountDue) {
			this.due = BigDecimal.valueOf(amountDue);
			return this;
		}
	}
}
