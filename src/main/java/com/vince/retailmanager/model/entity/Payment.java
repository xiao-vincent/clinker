package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
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
//@ValidPayment
public class Payment extends BaseEntity {

  @Min(0)
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
    ensureInvoiceNotVoid();
    ensureValidAmount();
    invoice.getPayments().add(this);
  }

  private void ensureValidAmount() {
    BigDecimal invoiceBalance = this.invoice.getBalance();
    if (amount.compareTo(invoiceBalance) > 0) {
      String msg =
          "Payment amount of " + amount + " is greater than remaining invoice balance of "
              + invoiceBalance;
      throw new InvalidOperationException(msg);
    }
  }

  private void ensureInvoiceNotVoid() {
    if (this.invoice.isVoid()) {
      throw new InvalidOperationException("Invoice is void");
    }
  }


  public static class PaymentBuilder {

    public PaymentBuilder amount(Double amount) {
      this.amount = BigDecimal.valueOf(amount);
      return this;
    }
  }

}
