package com.vince.retailmanager.model.entity.fees;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.BaseEntity;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.web.json.View;
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

/**
 * Domain entity used as a base class for percentage fees. A franchisor collects percentage fees
 * from its franchisees.
 *
 * @author Vincent Xiao
 * @see Franchisor
 */
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

  @NotNull
  @JsonView(View.PercentageFee.class)
  private double feePercent;

  @NotNull
  @JsonView(View.Summary.class)
  private String description;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "franchisee_id")
  @NotNull
  @JsonView(View.PercentageFee.class)
  private Franchisee franchisee;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "income_statement_id")
  @NotNull
  @JsonView(View.PercentageFee.class)
  private IncomeStatement incomeStatement;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "invoice_id")
  @NotNull
  @Valid
  @JsonView(View.PercentageFee.class)
  private Invoice invoice;

  PercentageFee(@NotNull String description, @NotNull IncomeStatement incomeStatement) {
    this.description = description;
    this.incomeStatement = incomeStatement;
    try {
      this.franchisee = (Franchisee) incomeStatement.getCompany();
    } catch (ClassCastException e) {
      throw new InvalidOperationException(incomeStatement.getCompany() + " is not a franchisee");
    }
    this.setFeePercent(this.getFranchisor().getMarketingFeePercent());
    this.setDefaultInvoice();
  }

  // Helper method
  protected Franchisor getFranchisor() {
    return this.franchisee.getFranchisor();
  }

  private double getBaseAmount() {
    if (this.getIncomeStatement() == null) {
      return 0.0;
    } else {
      return this.getIncomeStatement().getSales().doubleValue();
    }
  }


  @JsonProperty("feeAmount")
  @JsonView(View.Summary.class)
  public double getFeeAmount() {
    return this.getFeePercent() * this.getBaseAmount();
  }

  /*
  Creates a invoice with default values
   */
  private void setDefaultInvoice() {
    this.setInvoice(Invoice.builder()
        .due(this.getFeeAmount())
        .description(this.getDescription())
        .sender(this.franchisee.getFranchisor())
        .recipient(this.getFranchisee())
        .build());
  }


}
