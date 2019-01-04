package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vince.retailmanager.model.View.Summary;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "companies")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference
public abstract class Company extends BaseEntity {

  @JsonIgnore
  private boolean enabled = true;

  @JsonView(Summary.class)
  private String type = getClass().getSimpleName();

  @Column
  @JsonView(Company.class)
  private BigDecimal cashBalance = BigDecimal.valueOf(0);

//  @OneToMany(mappedBy = "recipient")
//  @JsonIgnore
//  private Set<Payment> paymentsReceived = new HashSet<>();
//
//  @OneToMany(mappedBy = "sender")
//  @JsonIgnore
//  private Set<Payment> paymentsSent = new HashSet<>();

  @OneToMany(mappedBy = "company")
  @JsonIgnore
  private Set<IncomeStatement> incomeStatements = new HashSet<>();

  public void addInvoiceReceived(Invoice invoice) {
//		invoicesReceived.add(invoice);
    invoice.setRecipient(this);
  }

  public void addInvoiceSent(Invoice invoice) {
//		invoicesSent.add(invoice);
    invoice.setSender(this);
  }

//  public void addPaymentReceived(Payment payment) {
////    paymentsReceived.add(payment);
//    payment.setRecipient(this);
//  }

//  public void addPaymentSent(Payment payment) {
////    paymentsSent.add(payment);
//    payment.setSender(this);
//  }


  public void addIncomeStatement(IncomeStatement incomeStatement) {
    incomeStatements.add(incomeStatement);
    incomeStatement.setCompany(this);
    setCashBalance(
        this.getCashBalance()
            .add(incomeStatement.getNetIncome())
    );
  }

}
