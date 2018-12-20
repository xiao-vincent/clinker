package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View.Public;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "percentage_fees")
@Data
@EqualsAndHashCode(callSuper = true, of = {""})
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fee_type")
public abstract class PercentageFee extends BaseEntity {

  @Column(name = "fee_type", insertable = false, updatable = false)
  private String feeType;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "franchisor_id")
  @NotNull
  @JsonView(Public.class)
  private Franchisor franchisor;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "franchisee_id")
  @NotNull
  @JsonView(Public.class)
  private Franchisee franchisee;
  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "income_statement_id")
  @NotNull
  @JsonView(Public.class)
  private IncomeStatement incomeStatement;
  @NotNull
  private double feePercent;
  @NotNull
  private String description;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "invoice_id")
  @NotNull
  @Valid
  @JsonView(Public.class)
  private Invoice invoice;

  void setAttributes(IncomeStatement incomeStatement) {
    if (incomeStatement == null) {
      throw new NullPointerException();
    }

    Franchisee franchisee = (Franchisee) incomeStatement.getCompany();
    this.setFranchisee(franchisee);
    this.setFranchisor(franchisee.getFranchisor());
    this.setIncomeStatement(incomeStatement);
  }

  //get payment amount and insert into invoice
  public double getBaseAmount() {
    if (this.getIncomeStatement() == null) {
      return 0.0;
    } else {
      return this.getIncomeStatement().getSales().doubleValue();
    }
  }

  public double getFee() {
    return this.getFeePercent() * this.getBaseAmount();
  }

  public void setDefaultInvoice() {
    this.setInvoice(Invoice.builder()
        .due(this.getFee())
        .description(this.getDescription())
        .sender(this.getFranchisor())
        .recipient(this.getFranchisee())
        .build());
  }

  protected void init() {
    this.setDefaultInvoice();
  }
}
