package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

	@JsonView(View.Public.class)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private BigDecimal balance;

	@JsonView(View.Public.class)
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

	@OneToMany(mappedBy = "invoice")
	@JsonView(View.Summary.class)
	@Builder.Default
	@JsonBackReference
	private Set<Payment> payments = new HashSet<>();

	@OneToMany(mappedBy = "invoice")
	@JsonView(View.Summary.class)
	@Builder.Default
	@JsonBackReference
	private Set<Payment> refunds = new HashSet<>();

	public BigDecimal getBalance() {
		if (payments.isEmpty()) {
			return due;
		} else {
			BigDecimal total = payments.stream()
				 .map(Payment::getAmount)
				 .reduce(BigDecimal.ZERO, BigDecimal::add);
			return due.subtract(total);
		}
	}

	public boolean isFullyPaid() {
		return this.getBalance().compareTo(BigDecimal.ZERO) <= 0;
	}


	public static class InvoiceBuilder {
		public InvoiceBuilder balance(Double amountDue) {
			this.due = BigDecimal.valueOf(amountDue);
			return this;
		}
	}
}
