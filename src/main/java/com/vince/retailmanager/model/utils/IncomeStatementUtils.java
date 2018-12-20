package com.vince.retailmanager.model.utils;

import com.vince.retailmanager.model.DateRange;
import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.utils.DateUtils;
import java.time.YearMonth;
import java.util.Set;
import java.util.stream.Collectors;

public class IncomeStatementUtils {

  public static Set<IncomeStatement> getIncomeStatementsInDateRange(Company company,
      DateRange dateRange
  ) {
    return company.getIncomeStatements()
        .stream()
        .filter(incomeStatement -> DateUtils
            .checkBetween(incomeStatement.getDate(), dateRange))
        .collect(Collectors.toSet());
  }

  public static Set<YearMonth> getMissingIncomeStatementDates(
      Company company,
      DateRange dateRange) {
    Set<YearMonth> result = DateUtils.getInclusiveRange(dateRange);
    Set<YearMonth> existentDates =
        company.getIncomeStatements().stream()
            .map(incomeStatement -> YearMonth.from(incomeStatement.getDate()))
            .collect(Collectors.toSet());
    result.removeAll(existentDates);
    return result;
  }
}
