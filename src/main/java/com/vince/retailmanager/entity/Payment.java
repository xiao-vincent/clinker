package com.vince.retailmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.joda.money.Money;

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
	private BigDecimal amount;

	//	@Basic
	@Builder.Default
	@Column
	private String currency = "USD";

	private transient Money money;

	@OneToOne
	@JoinColumn(name = "payer_id")
	@NotNull
	private Company sender;

	@OneToOne
	@JoinColumn(name = "recipient_id")
	@NotNull
	@JsonIgnoreProperties("franchisees")
	private Company recipient;

	public static class PaymentBuilder {
		public PaymentBuilder amount(Double amount) {
			this.amount = BigDecimal.valueOf(amount);
//			CurrencyUnit usd = CurrencyUnit.of("USD");
//			this.amount = Money.of(usd, amount);
			return this;
		}
	}

}
