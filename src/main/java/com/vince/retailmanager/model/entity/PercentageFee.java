package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.vince.retailmanager.model.View;
import com.vince.retailmanager.utils.EnumUtils;
import java.util.Map;
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

  public enum FeeType {
    ROYALTY(Constants.ROYALTY),
    MARKETING(Constants.MARKETING);

    FeeType(String val) {
      // force equality between name of enum instance, and value of constant
      if (!this.name().equals(val)) {
        throw new IllegalArgumentException("Incorrect use of FeeType");
      }
    }

    public static final Map<String, FeeType> NAME_MAP = EnumUtils.createNameMap(FeeType.class);

    public static FeeType fromString(String name) {
      return EnumUtils.fromString(NAME_MAP, name);
    }

    public static class Constants {

      public static final String ROYALTY = "ROYALTY";
      public static final String MARKETING = "MARKETING";
    }
  }

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
  @JoinColumn(name = "franchisor_id")
  @NotNull
  @JsonView(View.PercentageFee.class)
  private Franchisor franchisor;

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

  @JsonProperty("feeAmount")
  @JsonView(View.Summary.class)
  public double getFeeAmount() {
    return this.getFeePercent() * this.getBaseAmount();
  }

  public void setDefaultInvoice() {
    this.setInvoice(Invoice.builder()
        .due(this.getFeeAmount())
        .description(this.getDescription())
        .sender(this.getFranchisor())
        .recipient(this.getFranchisee())
        .build());
  }

  protected void init() {
    this.setDefaultInvoice();
  }
}
