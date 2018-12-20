package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import com.vince.retailmanager.web.controller.ValidPayment;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidPayment
public class Payment extends BaseEntity {

  @DecimalMin("0.00")
  @JsonView(Public.class)
  private BigDecimal amount;

  @Builder.Default
  @JsonView(Public.class)
  private String currency = "USD";

  @OneToOne
  @JoinColumn(name = "sender_id")
  @NotNull
  @JsonView(Summary.class)
  private Company sender;

  @OneToOne
  @JoinColumn(name = "recipient_id")
  @NotNull
  @JsonIgnoreProperties("franchisees")
  @JsonView(Summary.class)
  private Company recipient;

  @OneToOne
  @JsonView(Public.class)
  private Payment refundedPayment;

  @ManyToOne
  @JoinColumn(name = "invoice_id")
  @NotNull
  @JsonBackReference
  @JsonView(Public.class)
  private Invoice invoice;


  public void addInvoice(Invoice invoice) {
    this.invoice = invoice;
    if (refundedPayment == null) {
      invoice.getPayments().add(this);
    } else {
      invoice.getRefunds().add(this);
    }
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
