package com.vince.retailmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vince.retailmanager.exception.InvalidOperationException;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.model.utils.IncomeStatementUtils;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"incomeStatements"})
@Getter
public class IncomeStatementStatistics {

  private final DateRange dateRange;
  @JsonIgnore
  private Collection<IncomeStatement> incomeStatements;
  @NotNull
  private final Company company;

  private IncomeStatement aggregateIncomeStatement;
  private UnitStatistics sales;


  class UnitStatistics {

    private double total;
    private double average;
    private double growthRate; //average annual growth rate
    private Entry min = new Entry(Double.MAX_VALUE);
    private Entry max = new Entry(Double.MIN_VALUE);
  /*
  - min/max/averages
  - growth, calculation: (multiply growth for each period)^(1/n) - 1
  -
   */

    UnitStatistics(Collection<IncomeStatement> incomeStatements,
        Function<IncomeStatement, BigDecimal> mapper) {

      for (IncomeStatement incomeStatement : incomeStatements) {
        double value = mapper.apply(incomeStatement).doubleValue();
        this.total += value;

        //set min
        if (value < this.min.value) {
          this.min.setValue(value);
          this.min.setIncomeStatement(incomeStatement);
        }

        //set max
        if (value > this.max.value) {
          this.max.setValue(value);
          this.max.setIncomeStatement(incomeStatement);
        }
      }

      this.average = this.total / incomeStatements.size();
      // set growthRate

    }

    @Data
    @NoArgsConstructor
    class Entry {

      private IncomeStatement incomeStatement;
      private double value;

      Entry(double value) {
        this.value = value;
      }

      Entry(IncomeStatement incomeStatement, double value) {
        this.incomeStatement = incomeStatement;
        this.value = value;
      }
    }

  }

  public static IncomeStatementStatistics create(Company company, DateRange dateRange) {
    IncomeStatementStatistics result = new IncomeStatementStatistics(company, dateRange);
    result.checkMissingIncomeStatements();
    result.init();
    return result;
  }

  private IncomeStatementStatistics(Company company, DateRange dateRange) {
    this.company = company;
    this.dateRange = dateRange;
  }

  private void init() {
    this.incomeStatements = IncomeStatementUtils
        .getIncomeStatementsInDateRange(this.company, this.dateRange);

//    this.sales = new UnitStatistics(
//        this.incomeStatements.stream()
//            .map(incomeStatement -> incomeStatement.getSales().doubleValue())
//            .collect(Collectors.toList())
//    );

    this.aggregateIncomeStatement = getIncomeStatementsTotals(company);
  }

  private void checkMissingIncomeStatements() {
    Set<YearMonth> missingDates = IncomeStatementUtils
        .getMissingIncomeStatementDates(company, dateRange);
    if (!missingDates.isEmpty()) {
      throw new InvalidOperationException(
          "Missing income statements between " + dateRange
              .getStartDate() + " and " + dateRange.getEndDate(),
          IncomeStatementStatistics.class.getSimpleName(),
          Map.of("missingIncomeStatementDates", missingDates)
      );
    }
  }

  private IncomeStatement getIncomeStatementsTotals(Company company) {
    return IncomeStatement.builder()
        .company(company)
        .sales(sum(IncomeStatement::getSales))
        .costOfGoodsSold(sum(IncomeStatement::getCostOfGoodsSold))
        .operatingExpenses(sum(IncomeStatement::getOperatingExpenses))
        .generalAndAdminExpenses(sum(IncomeStatement::getGeneralAndAdminExpenses))
        .build();
  }

  private Double sum(Function<IncomeStatement, BigDecimal> mapper) {
    return incomeStatements
        .stream()
        .map(mapper)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .doubleValue()
        ;
  }


}
