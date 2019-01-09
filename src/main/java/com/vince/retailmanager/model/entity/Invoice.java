package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@Builder(builderClassName = "ObjectBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

  @Min(0)
  @JsonView(Summary.class)
  private BigDecimal due;

  @JsonView(Public.class)
  @Setter(AccessLevel.NONE)
  @Transient
  private BigDecimal balance;

  @JsonView(Summary.class)
  private boolean isVoid;

  @NotNull
  @JsonView(Summary.class)
  private String description;

  @OneToOne
  @JoinColumn(name = "sender_id")
  @NotNull
  @JsonView(View.Invoice.class)
  private Company sender;

  @OneToOne
  @JoinColumn(name = "recipient_id")
  @NotNull
  @JsonView(View.Invoice.class)
  private Company recipient;

  @OneToMany(mappedBy = "invoice")
  @Builder.Default
  @JsonManagedReference
  private Set<Payment> payments = new HashSet<>();

  public BigDecimal getBalance() {
    BigDecimal total = BigDecimal.ZERO;
    for (Payment payment : payments) {
      if (payment.getRefundedPayment() != null) {
        total = total.subtract(payment.getAmount());
      } else {
        total = total.add(payment.getAmount());
      }
    }
    return due.subtract(total);
  }

  public boolean isFullyPaid() {
    return this.getBalance().compareTo(BigDecimal.ZERO) <= 0;
  }

  public static class ObjectBuilder {

    public ObjectBuilder due(Double amountDue) {
      this.due = BigDecimal.valueOf(amountDue);
      return this;
    }
  }
}
