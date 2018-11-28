package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonView;
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
@EqualsAndHashCode(callSuper = true, of = {""})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

	@DecimalMin("0.00")
	@JsonView(View.Public.class)
	private BigDecimal due;

	@JsonView(View.Invoice.class)
	private transient BigDecimal balance;

	@JsonView(View.Public.class)
//	@NotNull
	private String description;

	@OneToOne
	@JoinColumn(name = "seller_id")
	@NotNull
	@JsonView(View.Public.class)
	private Company seller;

	@OneToOne
	@JoinColumn(name = "customer_id")
	@NotNull
	@JsonView(View.Public.class)
	private Company customer;

	@OneToOne
	@JoinColumn(name = "payment_id")
	@JsonView(View.Public.class)
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
