package com.vince.retailmanager.model.entity.transactions;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.entity.BaseEntity;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.web.json.View;
import com.vince.retailmanager.web.json.View.Public;
import com.vince.retailmanager.web.json.View.Summary;
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

/**
 * An invoice is issued by the seller to a buyer.
 *
 * @author Vincent Xiao
 */
@Entity
@Table(name = "invoices")
@Data
@EqualsAndHashCode(callSuper = true, of = "")
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


  BigDecimal getBalance() {
    BigDecimal totalPayments = BigDecimal.ZERO;
    for (Payment payment : payments) {
      //if the payment has been refunded, subtract from the total payment amount
      if (payment.getRefundedPayment() != null) {
        totalPayments = totalPayments.subtract(payment.getAmount());
      } else {
        totalPayments = totalPayments.add(payment.getAmount());
      }
    }
    return due.subtract(totalPayments);
  }

  public boolean isFullyPaid() {
    return this.getBalance().compareTo(BigDecimal.ZERO) <= 0;
  }


  /* Helper class for Lombok's @Builder api */
  public static class ObjectBuilder {

    public ObjectBuilder due(Double amountDue) {
      this.due = BigDecimal.valueOf(amountDue);
      return this;
    }
  }
}
