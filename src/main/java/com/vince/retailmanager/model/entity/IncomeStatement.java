package com.vince.retailmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vince.retailmanager.YearMonthConverter;
import com.vince.retailmanager.model.Validation;
import com.vince.retailmanager.model.View.Public;
import com.vince.retailmanager.model.View.Summary;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "income_statements", uniqueConstraints = @UniqueConstraint(columnNames = {"company",
    "date"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference
@Data
@EqualsAndHashCode(callSuper = true, of = {"company", "date"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Summary.class)
public class IncomeStatement extends BaseEntity {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
      CascadeType.REFRESH})
  @JoinColumn(name = "company")
  @NotNull(groups = {Validation.Entity.class})
  private Company company;

  @NotNull
  private BigDecimal sales;

  @NotNull
  private BigDecimal costOfGoodsSold;

  @Transient
  @Setter(AccessLevel.NONE)
  private BigDecimal grossProfit;

  @NotNull
  private BigDecimal operatingExpenses;

  @Transient
  @Setter(AccessLevel.NONE)
  private BigDecimal operatingIncome;

  @NotNull
  private BigDecimal generalAndAdminExpenses;

  @Transient
  @Setter(AccessLevel.NONE)
  private BigDecimal netIncome;

  @NotNull(groups = {Validation.Entity.class})
  @JsonView(Public.class)
  private LocalDate date;

  public BigDecimal getGrossProfit() {
    return sales.subtract(costOfGoodsSold);
  }

  public BigDecimal getOperatingIncome() {
    return getGrossProfit().subtract(operatingExpenses);
  }

  public BigDecimal getNetIncome() {
    return getOperatingIncome().subtract(generalAndAdminExpenses);
  }

  public void setDate(int year,
      int month) {
    this.date = YearMonthConverter.createEndOfMonthDate(year, month);
  }

  public static class IncomeStatementBuilder {

    public IncomeStatementBuilder sales(Double amt) {
      this.sales = BigDecimal.valueOf(amt);
      return this;
    }

    public IncomeStatementBuilder costOfGoodsSold(Double amt) {
      this.costOfGoodsSold = BigDecimal.valueOf(amt);
      return this;
    }

    public IncomeStatementBuilder operatingExpenses(Double amt) {
      this.operatingExpenses = BigDecimal.valueOf(amt);
      return this;
    }

    public IncomeStatementBuilder generalAndAdminExpenses(Double amt) {
      this.generalAndAdminExpenses = BigDecimal.valueOf(amt);
      return this;
    }

    public IncomeStatementBuilder date(int year,
        int month) {
      this.date = YearMonthConverter.createEndOfMonthDate(year, month);
      return this;
    }

//    private void grossProfit(BigDecimal grossProfit) {
//      this.grossProfit = grossProfit;
//    }
//
//    private void operatingIncome(BigDecimal operatingIncome) {
//      this.operatingIncome = operatingIncome;
//    }
//
//    private void netIncome(BigDecimal netIncome) {
//      this.netIncome = netIncome;
//    }
  }


}

