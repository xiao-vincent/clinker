package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
	//	@Basic
	@Column
	@DecimalMin("0.00")
	@JsonView(View.Summary.class)
	private BigDecimal amount;

	//	@Basic
	@Builder.Default
	@Column
	@JsonView(View.Summary.class)
	private String currency = "USD";

//	private transient Money money;

	@OneToOne
	@JoinColumn(name = "payer_id")
	@NotNull
	@JsonView(View.Summary.class)
	private Company sender;

	@OneToOne
	@JoinColumn(name = "recipient_id")
	@NotNull
	@JsonIgnoreProperties("franchisees")
	@JsonView(View.Summary.class)
	private Company recipient;


	@ManyToOne
	@JoinColumn(name = "invoice_id")
	@NotNull
//	@JsonManagedReference
	@JsonView(View.Payment.class)
	private Invoice invoice;


	public void addInvoice(Invoice invoice) {
		invoice.getPayments().add(this);
		this.invoice = invoice;
	}


	public static class PaymentBuilder {
		public PaymentBuilder amount(Double amount) {
			this.amount = BigDecimal.valueOf(amount);
//			CurrencyUnit usd = CurrencyUnit.of("USD");
//			this.amount = Money.of(usd, amount);
			return this;
		}
	}

}
